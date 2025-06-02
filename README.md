<h1>프론트엔드 구현 (담당자: 전동원)</h1>
애플리케이션의 기능은 네 가지로 나뉜다
1. 알약 이미지 검색
2. 알약 식별 검색
3. 즐겨찾기한 알약 보기
4. 알약 캘린더(복용 알람)

<h2>1. 알약 이미지 검색</h2>
<h3>입력으로는 다음과 같은 알약 사진의 앞, 뒷면을 입력받는다. 알약이 여러 개일 경우 좌에서 우로 순서대로 놓고 촬영하여야 한다.</h3>
![pill_back](https://github.com/user-attachments/assets/226b4bdc-7046-4189-98aa-6a20b6f534c4)
![pill_front](https://github.com/user-attachments/assets/a3dd1a7a-64ee-4558-bbf0-cacf3081c4c4)

결과로는 배경제거, YOLO 모델을 거친 후 OCR 모델 결과와 적절히 조합하여 결과 알약 하나를 반환한다.



<h2>2. 알약 식별 검색</h2>

![image](https://github.com/user-attachments/assets/8e745c46-3311-4020-8c99-518acac16b55)

위와 같이 알약을 직접 보고 알약의 특징을 입력하여 서버로 보내면 알약 리스트를 반환한다.


<h2>3. 즐겨찾기한 알약 보기</h2>

![image](https://github.com/user-attachments/assets/8aae982f-97af-42f0-bb17-9ee62f40e74a)

즐겨찾기한 알약을 확인할 수 있다. 각 알약의 상세페이지에서 즐겨찾기를 등록하면 된다.

<h2>4. 알약 캘린더(복용 알람)</h2>
![image](https://github.com/user-attachments/assets/30d20959-ef99-4496-85d8-e717022bbfb3)
먹어야 할 알약을 리스트로 보여주고 있을 경우 특정 시간에 알림을 보낸다.
