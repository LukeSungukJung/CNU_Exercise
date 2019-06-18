
% image 를 읽은 후
img = imread('as.jpg');

% my_bilinear 를 통해 이미지의 크기 조절
re = my_bilinear(img, 1000, 1000);

% 이미지 확인
figure
subplot(1, 1000, 1:470);
imshow(img);
title('base image');

subplot(1, 1000, 530:1000);
imshow(re);
title('resized image');
