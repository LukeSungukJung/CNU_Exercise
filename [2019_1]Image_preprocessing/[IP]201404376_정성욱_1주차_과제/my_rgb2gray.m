%as =imread('as.jpg');
%gray =  rgb2gray(a);
%���� rgb2gray�� ���ϱ�����;
%�Ʒ��� ���� ������ rgb2gray

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


