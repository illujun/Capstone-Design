import os
import json

# JSON들이 여러 폴더에 흩어져 있는 최상위 경로
json_root_dir = r"C:\Users\tkdwn\Downloads\166.약품식별 인공지능 개발을 위한 경구약제 이미지 데이터\01.데이터\2.Validation\라벨링데이터"
output_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\converted_jsons"

os.makedirs(output_dir, exist_ok=True)

def split_colors(color_str):
    if not color_str:
        return []
    return [c.strip() for c in color_str.replace(";", ",").split(",") if c.strip()]

def process_json_file(input_path, output_path):
    with open(input_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    categories = data.get("categories", [])
    cat_name_to_id = {cat["name"]: cat["id"] for cat in categories}

    # 모양, 색깔 수집
    shape_set = set()
    color_set = set()
    for img in data.get("images", []):
        shape = img.get("dl_custom_shape", "").strip()
        if shape:
            shape_set.add(shape)
        for color_field in ["color_class1", "color_class2"]:
            color_str = img.get(color_field, "")
            for c in split_colors(color_str):
                color_set.add(c)

    max_cat_id = max(cat_name_to_id.values()) if cat_name_to_id else 0

    for shape in sorted(shape_set):
        if shape not in cat_name_to_id:
            max_cat_id += 1
            cat_name_to_id[shape] = max_cat_id
            categories.append({"id": max_cat_id, "name": shape})

    for color in sorted(color_set):
        if color not in cat_name_to_id:
            max_cat_id += 1
            cat_name_to_id[color] = max_cat_id
            categories.append({"id": max_cat_id, "name": color})

    image_id_map = {img["id"]: img for img in data.get("images", [])}

    new_annotations = []

    for anno in data.get("annotations", []):
        new_annotations.append(anno)

        image_info = image_id_map.get(anno["image_id"])
        if not image_info:
            continue

        shape_name = image_info.get("dl_custom_shape", "").strip()
        if shape_name in cat_name_to_id:
            shape_anno = anno.copy()
            shape_anno["id"] = max([a["id"] for a in new_annotations]) + 1
            shape_anno["category_id"] = cat_name_to_id[shape_name]
            new_annotations.append(shape_anno)

        for color_field in ["color_class1", "color_class2"]:
            color_str = image_info.get(color_field, "")
            for color_name in split_colors(color_str):
                if color_name in cat_name_to_id:
                    color_anno = anno.copy()
                    color_anno["id"] = max([a["id"] for a in new_annotations]) + 1
                    color_anno["category_id"] = cat_name_to_id[color_name]
                    new_annotations.append(color_anno)

    data["categories"] = categories
    data["annotations"] = new_annotations

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

    print(f"변환 완료: {output_path}")

# 재귀적으로 JSON 파일 탐색 및 처리
for root, dirs, files in os.walk(json_root_dir):
    for file in files:
        if file.endswith(".json"):
            in_path = os.path.join(root, file)
            # 출력 파일명은 원본 위치 기반으로 고유하게 생성 (예: 폴더 이름+파일 이름)
            rel_path = os.path.relpath(in_path, json_root_dir)
            out_path = os.path.join(output_dir, rel_path)
            os.makedirs(os.path.dirname(out_path), exist_ok=True)

            process_json_file(in_path, out_path)
