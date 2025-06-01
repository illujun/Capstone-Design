from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
import requests
from rembg import remove, new_session
from ultralytics import YOLO
import numpy as np
import io
import math
from PIL import Image
from OCR_Process import ocr_images
from OCR_Improving import correct_ocr
from pathlib import Path
from datetime import datetime
import json

app = FastAPI()
API_URL = "http://127.0.0.1:9998/api/pill/lookup"  # 여기에 진짜 URL 입력

# ONNX 커스텀 모델 세션 초기화
model_path = "models/u2net_pill_136000.onnx"
session = new_session(model_name='u2net_custom', model_path=model_path)
yolo_model = YOLO('models/best.pt')

# IOU 계산 함수
def compute_iou(box1, box2):
    xA = max(box1[0], box2[0])
    yA = max(box1[1], box2[1])
    xB = min(box1[2], box2[2])
    yB = min(box1[3], box2[3])

    interArea = max(0, xB - xA) * max(0, yB - yA)
    boxAArea = (box1[2] - box1[0]) * (box1[3] - box1[1])
    boxBArea = (box2[2] - box2[0]) * (box2[3] - box2[1])
    union = boxAArea + boxBArea - interArea
    return interArea / union if union else 0


# 중복 박스 제거 함수
def deduplicate_boxes(boxes, iou_threshold=0.8):
    kept = []
    used = [False] * len(boxes)

    for i in range(len(boxes)):
        if used[i]:
            continue
        current = boxes[i]
        group = [current]
        used[i] = True
        for j in range(i + 1, len(boxes)):
            if used[j]:
                continue
            iou = compute_iou(current["box"], boxes[j]["box"])
            if iou > iou_threshold:
                group.append(boxes[j])
                used[j] = True
        # 그룹 중 신뢰도 높은 박스만 유지
        best = max(group, key=lambda b: b["confidence"])
        kept.append(best)
    return kept

# 중심점 계산
def center_of(box):
    x1, y1, x2, y2 = box
    return ((x1 + x2) / 2, (y1 + y2) / 2)

# 거리 계산
def euclidean(p1, p2):
    return math.sqrt((p1[0]-p2[0])**2 + (p1[1]-p2[1])**2)

@app.post("/upload")
async def upload_image(front: UploadFile = File(...), back: UploadFile = File(...)):
    input_bytes = await front.read()

    # 배경 제거 + 이미지 변환
    removed_bg = remove(input_bytes, session=session)
    image = Image.open(io.BytesIO(removed_bg)).convert("RGB")
    image_np = np.array(image)

    # YOLO 추론
    results = yolo_model.predict(source=image_np, conf=0.25, save=True, save_txt=True)

    # YOLO 박스 추출
    raw_boxes = []
    for box in results[0].boxes:
        cls_id = int(box.cls[0])
        confidence = float(box.conf[0])
        x1, y1, x2, y2 = map(float, box.xyxy[0])
        raw_boxes.append({
            "class_id": cls_id,
            "confidence": confidence,
            "box": [x1, y1, x2, y2]
        })

    # 중복 제거
    deduped_boxes = deduplicate_boxes(raw_boxes)

    # X좌표 기준 정렬 및 라벨링
    deduped_boxes.sort(key=lambda b: center_of(b["box"])[0])
    for idx, box in enumerate(deduped_boxes):
        box["label"] = str(idx + 1)

    
    # 저장 폴더 생성
    temp_dir = Path("temp_uploads")
    temp_dir.mkdir(parents=True, exist_ok=True)

    # 파일 저장 경로 생성 (timestamp 활용)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    front_path = temp_dir / f"front_{timestamp}.png"
    back_path = temp_dir / f"back_{timestamp}.png"

    # front 저장
    front_image = Image.open(io.BytesIO(input_bytes)).convert("RGB")
    front_image.save(front_path)

    # back 저장
    back_bytes = await back.read()
    back_image = Image.open(io.BytesIO(back_bytes)).convert("RGB")
    back_image.save(back_path)

    print(f"[파일 저장 완료] front: {front_path}, back: {back_path}")

    image_files = [front_path, back_path] # 파일 경로

    ocr_results = ocr_images(image_files, "vision_result")
    ocr_improve_results = correct_ocr(ocr_results)
    print(ocr_improve_results)

    print(f"YOLO 원본 박스 수: {len(raw_boxes)}")
    print(f"YOLO 중복 제거 후 박스 수: {len(deduped_boxes)}")
    for i, box in enumerate(deduped_boxes):
        print(f"[{i+1}] class_id={box['class_id']}, box={box['box']}")
    # 클래스 ID → 이름 변환 딕셔너리
    class_id_to_name = {
        0: '갈색_타원형', 1: '갈색_원형', 2: '갈색_장방형', 3: '갈색_캡슐형', 4: '갈색_기타',
        5: '노랑_타원형', 6: '노랑_원형', 7: '노랑_장방형', 8: '노랑_캡슐형', 9: '노랑_기타',
        10: '빨강_타원형', 11: '빨강_원형', 12: '빨강_장방형', 13: '빨강_캡슐형', 14: '빨강_기타',
        15: '초록_타원형', 16: '초록_원형', 17: '초록_장방형', 18: '초록_캡슐형', 19: '초록_기타',
        20: '파랑_타원형', 21: '파랑_원형', 22: '파랑_장방형', 23: '파랑_캡슐형', 24: '파랑_기타',
        25: '하양_타원형', 26: '하양_원형', 27: '하양_장방형', 28: '하양_캡슐형', 29: '하양_기타',
        30: '기타_기타'
    }

    # OCR 결과 매핑 및 JSON 로그 생성
    try:
        # YOLO 박스 기준 결과 초기화
        matched_result = []
        for box in deduped_boxes:
            matched_result.append({
                "label": box["label"],
                "class": class_id_to_name.get(box["class_id"], f"UNKNOWN_{box['class_id']}"),
                "front": None,
                "back": None
        })

        total_ocr_count = 0

        for filename, items in ocr_improve_results.items():
            direction = "front" if "front" in filename else "back"
            total_ocr_count += len(items)

            for item in items:
                pts = item["box"]
                cx = sum(p[0] for p in pts) / len(pts)
                cy = sum(p[1] for p in pts) / len(pts)
                center = (cx, cy)

                best_match = min(matched_result, key=lambda b: euclidean(center, center_of(deduped_boxes[int(b["label"]) - 1]["box"])))

                if best_match[direction] is None:
                    best_match[direction] = item["text"]

        # 상태 코드 판단 (front/back 각각 YOLO 수보다 많으면 비정상)
        num_yolo = len(matched_result)
        num_front = sum(
            len(items)
            for name, items in ocr_improve_results.items()
            if "front" in name
        )
        num_back = sum(
            len(items)
            for name, items in ocr_improve_results.items()
            if "back" in name
        )

        if num_front > num_yolo or num_back > num_yolo:
            status_code = 1  # OCR 개수가 YOLO보다 많은 경우
        else:
            status_code = 2  # 정상

        log_json = {
            "status": status_code,
            "results": matched_result
        }

    except Exception as e:
        log_json = {
            "status": 0,
            "error": str(e)
        }

    print(json.dumps(log_json, ensure_ascii=False, indent=2))

    # 새 결과 리스트
    final_results = []
    success_count = 0

    for item in log_json["results"]:
        try:
            response = requests.post(API_URL, json=item, timeout=5)
            if response.status_code == 200:
                result = response.json()
                final_results.append(result)
                success_count += 1
            else:
                final_results.append(None)
        except Exception as e:
            final_results.append(None)

    # status 결정
    if success_count == len(log_json["results"]):
        final_status = 2
    elif success_count > 0:
        final_status = 1
    else:
        final_status = 0

    # log_json 덮어쓰기
    log_json = {
        "status": final_status,
        "results": final_results
    }

    # 출력
    print(json.dumps(log_json, ensure_ascii=False, indent=2))
    return JSONResponse(content=log_json)
