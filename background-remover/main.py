from rembg.session_factory import new_session

model_path = "F:\\GitHub\\Capstone-Design\\background-remover\\u2net_pill_74000.onnx"
session = new_session(model_path)

# 모델이 잘 로드되었는지 확인
print(session)  # 출력에 model_path가 포함되어야 함
