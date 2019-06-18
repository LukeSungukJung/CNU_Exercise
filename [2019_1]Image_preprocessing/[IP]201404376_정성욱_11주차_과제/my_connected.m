function img = my_connected(img)
% Find connected level using DFS for 4-direction
% img       : binary image

% Set Limit of recursion 3000
set(0, 'RecursionLimit', 3000)

% Set 1 to -1
[x,y] = size(img);
img = img > 108;
% recursive find 1 value
img = double(img);
for i =1:x
    for j = 1:y
        if(img(i,j)==1)
        img(i,j)=-1;
        end
    end
end
pad_img = zeros(x+1,y+1);
pad_img(2:x+1,2:y+1)= img;

res = my_recursive_label(pad_img,2,2,x,y,0);

img = uint8(res);
end

function res = my_recursive_label(img, x, y, width,height, c)
% Recursively Find 1
% img    : binary image
% y      : y index
% x      : x index
% height : height of image
% width  : width of image
% c      : value for labeling 
rc_mode=1;
if(img(x-1,y)<=0&&img(x+1,y)<=0&&img(x,y-1)<=0&&img(x,y+1)<=0)
    rc_mode = 0;
end
if(rc_mode==1)
    if(img(x,y)==-1 &&x==width&&y==height)
    img(x,y)=255/c;
    end
else
for ix=x:width
    for iy=y:height
        
        if(img(ix,iy+1)==-1)
            img = my_recursive_label(img, ix, iy+1, ix, iy+1, c);        end
        if(img(ix,iy-1)==-1)
            img = my_recursive_label(img, ix, iy-1, ix, iy-1, c);        end
        if(img(ix+1,iy)==-1)
            img = my_recursive_label(img, ix+1, iy,  ix+1, iy, c);        end
        if(img(ix-1,iy)==-1)
            img = my_recursive_label(img, ix-1, iy, ix-1, iy, c);        end
    end
end
end
% Recursively find -1 and ignore 0
% Recursion in matlab should receive result of 'call by value'
res = img;
end