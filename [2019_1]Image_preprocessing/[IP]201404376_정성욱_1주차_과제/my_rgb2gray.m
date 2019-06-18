%as =imread('as.jpg');
%gray =  rgb2gray(a);
%원래 rgb2gray와 비교하기위해;
%아래는 내가 정의한 rgb2gray

function res =rgb2gray(a)
%width = size(a);
%width = width(1);
%height = size(a);
%height = height(1);
%res = zeros(width,height);

aa = a(:,:,1)*0.2;
bb = a(:,:,2)*0.4;
cc = a(:,:,3)*0.4;
res = aa+bb+cc-50;
end


