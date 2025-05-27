import os
import shutil
import random

# 평탄화된 이미지/라벨 폴더
flat_img_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\images_flat"
flat_label_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\labels_flat"

# 최종 train/val 폴더 경로
train_img_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\images\train"
val_img_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\images\val"

train_label_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\labels\train"
val_label_dir = r"C:\Users\tkdwn\PycharmProjects\YOLOtest\labels\val"

# 폴더 없으면 생성
os.makedirs(train_img_dir, exist_ok=True)
os.makedirs(val_img_dir, exist_ok=True)
os.makedirs(train_label_dir, exist_ok=True)
os.makedirs(val_label_dir, exist_ok=True)

# 이미지 파일 리스트
image_files = [f for f in os.listdir(flat_img_dir) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]

random.seed(42)
random.shuffle(image_files)

# 20%를 val로 분할
val_count = int(len(image_files) * 0.2)
val_files = image_files[:val_count]
train_files = image_files[val_count:]

print(f"전체 {len(image_files)}개 중 {val_count}개를 검증용으로 분리")

def move_files(file_list, img_src, img_dst, label_src, label_dst):
    for file_name in file_list:
        shutil.move(os.path.join(img_src, file_name), os.path.join(img_dst, file_name))

        label_name = os.path.splitext(file_name)[0] + ".txt"
        src_label_path = os.path.join(label_src, label_name)
        dst_label_path = os.path.join(label_dst, label_name)

        if os.path.exists(src_label_path):
            shutil.move(src_label_path, dst_label_path)
        else:
            print(f"라벨 없음: {src_label_path}")

move_files(train_files, flat_img_dir, train_img_dir, flat_label_dir, train_label_dir)
move_files(val_files, flat_img_dir, val_img_dir, flat_label_dir, val_label_dir)

print("✅ train/val 분할 완료!")
