

function zigzag = my_encoding(img)

    function C=set_C(w,n)
        if(w==0)
            C=sqrt(1/n);
        else
            C=sqrt(2/n);
        end
    end

    function [until_zero_list,zero_index] = get_zero_idx(ttt)
         [ti,tl] =size(ttt);
        this_index = 1;
        for ki = 1:tl
            if(ttt(ki)~=0)
                this_index = ki;
            end
        end
       zero_index=this_index;
       until_zero_list = ttt(1:this_index);
    end

    function zz= make_zigzag(dct_list)
        list = [];
    idx=1;
    idy=1;
    count=1;
    decrease = false;
    increase = true;
    mode_change=false;
    while true
        [chkx,chky] =  size(list);
        if(chky==64) break
        end

        if(mode_change==false)
          if(idy==1 && idx==1)
             list(count) = dct_list(idx,idy);
             count=count+1;
            idy=idy+1;
            decrease = true;
            increase = false; 
          elseif(decrease)
            idx=1;
            for iy = idy:-1:1
              list(count) = dct_list(idx,iy);
              idx=idx+1;
              count=count+1;
            end
            decrease = false;
            increase = true;
            elseif(increase)
              idy=1;
              for ix = idx:-1:1
                if(ix>8) 
                    idy=2;
                    idx=8;
                    mode_change=true;
                    decrease = false;
                    increase = true;
                    break;
                end
                list(count) = dct_list(ix,idy);
                count=count+1;
                idy=idy+1;
              end
            if(mode_change==false)
            decrease = true;
            increase = false;
            end
          end
        else
        if(decrease)   
            laci=0;  
            ix =  idx;
            for aci =idy:-1:idx
              list(count) =  dct_list(ix,aci);
              ix=ix+1;
              count=count+1;
              laci=aci;
            end

            if(idx>8) idx=8; end
              tmp = idx+1;
              idx = 8;
              idy = tmp;
            decrease = false;
            increase = true;    

        elseif(increase)
            laci = 0;
            iy=idy;
          for aci =idx:-1:idy  
              list(count) =  dct_list(aci,iy);
              iy=iy+1;
              count=count+1;
               laci=aci;
          end
          if(idy>8) idy=8; end
              tmp = idx;
              idx = idy+1;
              idy = 8;
            decrease = true;
            increase = false;  

        end
    end  
    end
    zz=list;
    end
% Compress Image using portion of JPEG
% img    : GrayScale Image
% zigzag : result of zigzag scanning
[x,y] =size(img);
rx =(mod(x,8));
ry =(mod(y,8));
x_padded=0;
y_padded=0;

if(not (rx==0))
    x_padded=(8-rx);
    rx = floor(x_padded/2);
end
if(not (ry==0))
    y_padded=(8-ry);
    ry = floor(y_padded/2);
end

pad_img = zeros(x+x_padded,y+y_padded);

if((x_padded==0)&&(y_padded==0))
    pad_img = img;
elseif((x_padded~=0&&y_padded~=0))
    pad_img(1:x,1:y) = uint8(img);
elseif(x_padded~=0)
    pad_img(1:x,1:y) = uint8(img);
elseif(y_padded~=0)
    pad_img(1:x,1:y) = uint8(img);
end


% Subtract 128
s_img =  double(pad_img) - 128;

DCT_table = ([[16,11,10,16,24,40,51,61]; 
              [12,12,14,19,26,58,60,55];
              [14,13,16,24,40,57,69,56];
              [14,17,22,29,51,87,80,62];
              [18,22,29,51,87,80,51,62];
              [24,35,55,64,81,104,113,92];
              [49,64,78,87,103,121,120,101];
              [72,92,95,98,112,100,103,99];]);
% Apply DCT
[px,py] = size(s_img);
step_x = px/8; step_y = py/8;
F_uv = zeros(64,64);


for i = 1:step_x:px
    u= floor(i/step_x)+1;
    for j= 1:step_y:py
        v= floor(j/step_y)+1;
        for lx = i:step_x+i-1;
            for ly = j :step_y+j-1;
                this_sum = zeros(8,8);
                for ix = 0:7
                    for iy = 0:7
                     this_sum(ix+1,iy+1) = this_sum(ix+1,iy+1) +cos((2*(ix+lx-1)+1)*(u-1)*pi/(2*8))*cos((2*(iy+ly-1)+1)*(v-1)*pi/(2*8));
                    end
                end
                F_uv((u-1)*8+1:(u)*8,(v-1)*8+1:(v)*8)=set_C(u,8)*set_C(v,8)*this_sum;
            end
        end
    end
end

DCT_res = zeros(px,py);
for ix = 1:8:px
    for iy = 1:8:py
        this_sum = zeros(8,8);
        for fi  = 1:8:64
            for fj = 1:8:64
            this_sum =this_sum+s_img(ix:ix+7,iy:iy+7).*F_uv(fi:fi+7,fj:fj+7);
            end
        end
        DCT_res(ix:ix+7,iy:iy+7) = round(this_sum./DCT_table);
    end
end


% Quantize Image using Qunatization Table
%round(this_sum./DCT_table);

list_zigzag= {};
% Zigzag scanning
aa=1;
for i = 1: 8:px
    for j = 1:8:py
    [no0_list,index] = get_zero_idx(make_zigzag(DCT_res(i:i+7,j:j+7)));
    list_zigzag{aa} = no0_list;
    aa=aa+1;
    list_zigzag{aa} = index;
    aa=aa+1;
    end
end






zigzag= list_zigzag;
end

