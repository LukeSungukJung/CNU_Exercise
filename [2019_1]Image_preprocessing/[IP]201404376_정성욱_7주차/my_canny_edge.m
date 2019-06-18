function edge_image = my_canny_edge(img, low_th, high_th, filter_size)
% Find edge of Image using canny edge detection
% img         : Grayscale image    dimension ( height x width )
% low_th      : low threshold      type ( uint8 )
% high_th     : high threshold     type ( uint8 )
% filter_size : size of filter     type ( int64 )
% edge_image  : edge of input img  dimension ( height x width )
[x, y] = size(img);
% 이전의 Gaussian Filter 사용
filtered_img= my_gaussian(img,filter_size,1);
% 이전의 Sobel을 응용한 함수 사용

    
pad_size = floor(filter_size/2);
    v_img = zeros(x, y);
    h_img = zeros(x, y);
    v_mask = [1:pad_size+1 pad_size:-1:1]' * [-pad_size:pad_size];
    h_mask = v_mask';
  v_img = conv2(double(filtered_img), v_mask, 'same');
  h_img = conv2(double(filtered_img), h_mask, 'same');
% 모든 위치의 Magnitude, Angle 계산
M_img = sqrt((v_img).^2+(h_img).^2);
Angle_array = atan2(h_img,v_img);
arah = Angle_array*180/pi;
for i=1:x
    for j=1:y
        if (arah(i,j)<0) 
            arah(i,j)=360+arah(i,j);
        end;
    end;
end;


angle_dir = zeros(size(img));

for i = 1  : x
    for j = 1 : y
        if(arah(i,j)==45 || arah(i,j)==225)
            angle_dir(i,j)=45;
        elseif(arah(i,j)==0 || arah(i,j) ==180 || arah(i,j)==360)
            angle_dir(i,j)=0;
        elseif(arah(i,j)==90 || arah(i,j) ==270)
            angle_dir(i,j)=90;         
        elseif(arah(i,j)==135 || arah(i,j) ==315)
            angle_dir(i,j)=135;
            
        elseif((arah(i,j)<45 && arah(i,j)>0) || (arah(i,j)>315 && arah(i,j)<360) )
            angle_dir(i,j)=1;     
        elseif((arah(i,j)<90 && arah(i,j)>45) || (arah(i,j)>90 && arah(i,j)<135) )
            angle_dir(i,j)=2;
        elseif((arah(i,j)<180 && arah(i,j)>135) || (arah(i,j)>180 && arah(i,j)<225) )
            angle_dir(i,j)=3;
        elseif((arah(i,j)<270 && arah(i,j)>225) || (arah(i,j)>270 && arah(i,j)<315) )
            angle_dir(i,j)=4;
        end;
    end;
end;
% Non-Maximum 값들 제거 (linear 방식)
ZM_img = zeros(x+1,y+1);
ZM_img(2:x+1,2:y+1) = M_img;
NM_img =zeros(size(img));
tan_cs= tan(arah/180*pi);

for ix = 2:x
    for iy= 2:y
        if(angle_dir(ix-1,iy-1)==0)
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),ZM_img(ix-1,iy),ZM_img(ix+1,iy)]);
        elseif(angle_dir(ix-1,iy-1)==45)
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),ZM_img(ix-1,iy-1),ZM_img(ix+1,iy+1)]);
        elseif(angle_dir(ix-1,iy-1)==90)
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),ZM_img(ix,iy-1),ZM_img(ix,iy+1)]);
        elseif(angle_dir(ix-1,iy-1)==135)
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),ZM_img(ix-1,iy+1),ZM_img(ix+1,iy-1)]);
        
        elseif(angle_dir(ix-1,iy-1)==1|| angle_dir(ix-1,iy-1)==3)
            
            jb = (iy-1)-tan_cs(ix-1,iy-1)*(ix-1);
            moved_y = tan_cs(ix-1,iy-1)*(ix)+jb;
            s = abs(moved_y-(iy-1));
            if((arah(ix-1,iy-1)>0 && arah(ix-1,iy-1)<45) ||(arah(ix-1,iy-1)>0 && arah(ix-1,iy-1)<225))
            res_p = s*ZM_img(ix+1,iy+1) + (1-s)*ZM_img(ix+1,iy);
            res_m = s*ZM_img(ix-1,iy-1) + (1-s)*ZM_img(ix-1,iy);
            else  
            res_p = s*ZM_img(ix+1,iy-1) + (1-s)*ZM_img(ix+1,iy);
            res_m = s*ZM_img(ix-1,iy+1) + (1-s)*ZM_img(ix-1,iy) ;           
            end
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),res_m,res_p]);
            
        elseif(angle_dir(ix-1,iy-1)==2|| angle_dir(ix-1,iy-1)==4)
            jb = (iy-1)-tan_cs(ix-1,iy-1)*(ix-1);
            moved_x = (iy-jb)/tan_cs(ix-1,iy-1);
            s = abs(moved_x-(ix-1));
            
            if((arah(ix-1,iy-1)>45 && arah(ix-1,iy-1)<90) ||(arah(ix-1,iy-1)>225 && arah(ix-1,iy-1)<270))
            res_p = s*ZM_img(ix+1,iy+1) + (1-s)*ZM_img(ix,iy+1);
            res_m = s*ZM_img(ix-1,iy-1) + (1-s)*ZM_img(ix,iy-1);
            
            else  
            res_p = s*ZM_img(ix-1,iy+1) + (1-s)*ZM_img(ix,iy+1);
            res_m = s*ZM_img(ix-1,iy-1) + (1-s)*ZM_img(ix,iy-1) ;           
            end
            NM_img(ix-1,iy-1) = ZM_img(ix,iy)==max([ZM_img(ix,iy),res_m,res_p]);   
        end
    end
end
NM_img = NM_img.*M_img;
% Double Threshold 계산
T_res = zeros (x, y);
for i = 2  : x-1
    for j = 2 : y-1
        if (NM_img(i, j) < low_th)
            T_res(i, j) = 0;
        elseif (NM_img(i, j) > high_th)
            T_res(i, j) = 1;
        elseif ( NM_img(i+1,j)>high_th || NM_img(i-1,j)>high_th || NM_img(i,j+1)>high_th || NM_img(i,j-1)>high_th || NM_img(i-1, j-1)>high_th || NM_img(i-1, j+1)>high_th || NM_img(i+1, j+1)>high_th || NM_img(i+1, j-1)>high_th)
            T_res(i,j) = 1;
        end;
    end;
end;

edge_image = uint8(T_res.*255);
end