from ultralytics import YOLO

def main():
    model = YOLO('yolov8n.pt')  # 사전학습된 초경량 모델

    model.train(
        data='pill.yaml',  # 데이터셋 설정 파일
        epochs=3,         # 충분한 학습을 위해 50 에폭 (테스트는 3~5로 줄여도 됨)
        imgsz=640,
        batch=16,          # GPU 환경에 맞게 조절
        device='cpu',      # GPU 없으면 'cpu'로 변경
        save=True
    )

if __name__ == "__main__":
    main()
