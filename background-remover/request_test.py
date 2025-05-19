import requests

url = "http://localhost:1321/upload"  # 서버 주소
front_image_path = "test_images/6.jpg"
back_image_path = "test_images/7.jpg"  # 더미 파일 필요

with open(front_image_path, 'rb') as front_file, open(back_image_path, 'rb') as back_file:
    files = {
        'front': ('6.jpg', front_file, 'image/jpeg'),
        'back': ('blank.jpg', back_file, 'image/jpeg')  # FastAPI에 요구되므로 더미라도 필요
    }
    response = requests.post(url, files=files)

    if response.status_code == 200:
        with open("result_front_nobg.png", "wb") as f:
            f.write(response.content)
        print("✅ 이미지 저장 완료: result_front_nobg.png")
    else:
        print(f"❌ 오류 발생: {response.status_code}")
