function re_img = my_bilinear(img,row,col)
  img = rgb2gray(img);
  re_img = zeros(row,col);
  [x,y] = size(img);
  pad_img = zeros(x+2,y+2);
  pad_img(2:x+1,2:y+1) = img;
  
  pad_img(1,1) = img(1,1);
  pad_img(1,y+2) = img(1,y);
  pad_img(x+2,1) = img(x,1);
  pad_img(x+2,y+2) =img(x,y);
  
  pad_img(2:x+1,1)=img(1:x,1);
  pad_img(2:x+1,y+2)=img(1:x,y);
  pad_img(1,2:y+1) = img(1,1:y);
  pad_img(x+2,2:y+1) = img(x,1:y);
  img = uint8(pad_img);
  r_p = x/row;
  c_p = y/col;
  
  for i = 1:row
    for j=1:col
      x_b = floor(i*r_p)+1;
      y_b = floor(j*c_p)+1;
      x_t = floor(i*r_p)+2;
      y_t = floor(j*c_p)+2;
      s = abs(i*r_p-x_b);
      t = abs(j*c_p-y_b);
      xbyb=(1-s)*(1-t)*img(x_b,y_b);
      xtyt=(s)*(t)*img(x_t,y_t);
      xtyb=((s)*(1-t)*img(x_b,y_t));
      xbyt=((1-s)*(t)*img(x_b,y_t));
      re_img(i,j) = xbyb+xtyt+xtyb+xbyt; 
    end
  end
  re_img = uint8(re_img);
  end