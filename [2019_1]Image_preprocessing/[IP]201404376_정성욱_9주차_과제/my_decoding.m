function img = my_decoding(zigzag)

    function C=set_C(w,n)
        if(w==0)
            C=sqrt(1/n);
        else
            C=sqrt(2/n);
        end
    end

     function res_batch =  layzigzag(i_list)
    re_batch=  zeros(8,8);
    [lx,ly] = size(i_list);
    list = zeros(1,64);
    list(1:ly) = i_list;

    count = 1;
    idx=1; idy=1;
    mode_change=false;
    while(true)
        if(mode_change~=true)
        if(idx==1&&idy==1)
                re_batch(idx,idy) = list(count);
                count = count+1;
                idy=idy+1;
                descent = true;
                accsent = false;
        elseif(descent==true)
            ix=idx;
            for iy = idy:-1:idx
                re_batch(ix,iy) = list(count);
                ix=ix+1;
                count = count+1;    
            end
            descent = false;
            accsent = true;
            idx=ix;
        else
            tmp=idx;
            ix =idx;
            idy=1;
            for iy = idy:idx
                   if(count==37)
                    count+1; 
                   mode_change=true;
                    descent = false;
                    accsent = true;
                    idx=8;
                    idy=2;
                    break;
                 end

                re_batch(ix,iy) = list(count);
                ix=ix-1;
                count = count+1;    
            end
                     if(mode_change) continue 
                   end
            descent = true;
            accsent = false;
            tmp=tmp+1;
            idy=tmp;
            idx=1;
        end
        else
            endw=false;

            if(descent)
            ix=idx;
            idx;
            idy;
            for iy = idy:-1:idx
                re_batch(ix,iy) = list(count);
                count=count+1;
                ix=ix+1;
            end
            descent = false;
            accsent = true;
            tmp = idx;
            idx = idy;
            idy =tmp +1;
        else
            tmp=idx;
            ix =idx;
            for iy = idy:idx
                if(count==64)
                    re_batch(ix,iy) = list(count);
                    endw=true;
                    break;
                end
                re_batch(ix,iy) = list(count);
                ix=ix-1;
                count = count+1;    
            end
            descent = true;
            accsent = false;
            tmp=idy;
            idy =idx;
            idx =tmp+1;

            end
        if(endw==true)
            break;
        end
        end
    end
    res_batch = re_batch;
    end

 function ss = inverse_dct(s_img)
    [px,py] = size(s_img);
    
    inverse_img = zeros(size(s_img));
    F_uv=zeros(8,8);
    for i = 0:8:px-1
        for j = 0:8:py-1
        F_uv = s_img(i+1:i+8,j+1:j+8);
        this_sum = zeros(8,8);
        for u = 0:7
            for v = 0:7
           this_sum(u+1,v+1)=set_C(u,8)*set_C(v,8)*cos((2*(i)+1)*(u)*pi/(2*8))*cos((2*(j)+1)*(v)*pi/(2*8));
            end
        end
        inverse_img(i+1:i+8,j+1:j+8) =this_sum.*F_uv;
        end
    end
            
            
    ss=inverse_img;
 end


DCT_table = ([[16,11,10,16,24,40,51,61]; 
              [12,12,14,19,26,58,60,55];
              [14,13,16,24,40,57,69,56];
              [14,17,22,29,51,87,80,62];
              [18,22,29,51,87,80,51,62];
              [24,35,55,64,81,104,113,92];
              [49,64,78,87,103,121,120,101];
              [72,92,95,98,112,100,103,99];]);

% Compress Image using portion of JPEG
% zigzag : result of zigzag scanning
% img    : GrayScale Image 
[ax,ay] = size(zigzag);
pxy = sqrt(ay/2);
s_img =  zeros(pxy*8,pxy*8);
res = s_img;

for iax = 1:2:ay/2
    tmp = layzigzag(zigzag{iax});
    for ix = 1:8:pxy*8
        for iy= 1:8:pxy*8
           s_img(ix:ix+7,iy:iy+7) = tmp.*DCT_table;
        end
    end
end
fxy = inverse_dct(s_img);
    %inverse_dct
    

% Construct 8x8 blocks using zigzag scanning value

% Multiply Quantization Table


% Apply Inverse DCT


% Add 128

img = uint8(fxy+128);
end

