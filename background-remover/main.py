from rembg import remove
from PIL import Image
import io

# 커스텀 모델 경로 설정
from rembg.session_factory import new_session
session = new_session(model_name='u2net_custom', model_options={'model_path': 'F:\\GitHub\\Capstone-Design\\models\\u2net_pill_136000.onnx'})

# 이미지 불러오기
with open('4.jpg', 'rb') as i:
    input_data = i.read()

# 배경제거
output_data = remove(input_data, session=session)

# 저장
with open('4_nobg_136000.png', 'wb') as o:
    o.write(output_data)
