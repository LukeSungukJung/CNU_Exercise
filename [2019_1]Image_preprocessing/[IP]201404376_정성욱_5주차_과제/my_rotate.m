function re_img = my_rotate(img, rad, interpolation)
% Fill here.
c = cos(rad);
s = sin(rad);
[x, y] = size(img);
f = [c s; -s c];
 
row = int64(abs(c)*x+abs(s)*y); %fill 
col = int64(abs(c)*y+abs(s)*x); %fill
re_img = zeros(row, col);

x_center =round(row/2);
y_center =round(col/2);
 
if strcmp(interpolation, 'nearest')
    for i = 1:row
       for j = 1:col
           v = f * double([(i-x_center);(j-y_center)]);
           
           v(1) = v(1)+round(x/2);
           v(2) = v(2)+round(y/2);
          if v(1) < 1 || v(1) > x || v(2) < 1 || v(2) > y
                   continue;
          end       
           re_img(i, j) = img(int64(v(1)), int64(v(2)));
       end
    end
    % Fill here.
elseif strcmp(interpolation, 'bilinear')
    pad_img = zeros(x+2,y+2);
    pad_img(2:x+1,2:y+1) = img;
     for i = 1:row
       for j = 1:col
           v = f * double([(i-x_center);(j-y_center)]);
           v(1) = v(1)+round(x/2);
           v(2) = v(2)+round(y/2);
          if v(1) < 1 || v(1) > x || v(2) < 1 || v(2) > y
                   continue;
          end       
          s = abs(v(1)-round(v(1)));
          t = abs(v(2)-round(v(2)));          
           re_img(i, j) = (1-s)*(1-t)*pad_img(floor(v(1)),floor(v(2)))+(s)*(t)*pad_img(floor(v(1))+1,floor(v(2))+1)+(1-s)*(t)*pad_img(floor(v(1)),floor(v(2))+1)+(s)*(1-t)*pad_img(floor(v(1))+1,floor(v(2)));
       end
    end
end
re_img = uint8(re_img);
end