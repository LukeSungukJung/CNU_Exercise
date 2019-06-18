function filter_img = my_bilateral(img, filter_size, sigma, sigma2)
% Apply bilateral filter
% img        : GrayScale Image
% filter_img : bilateral filtered image 

[ox,oy] = size(img);

pad_size = floor(filter_size/2);
pad_img = double(my_padding(img,pad_size,'mirror'));
[px,py] = size(pad_img);
filter_img = zeros(size(img));
for i = 1+pad_size:px-pad_size
    for j = 1+pad_size:py-pad_size
        filter=  zeros(filter_size,filter_size);
        ci_start =i-pad_size;  	 ci_end =i+pad_size;
        cj_start =j-pad_size;    cj_end =j+pad_size; 

        fi=1;
        for tk = ci_start:ci_end
        fj=1;
        for tl = cj_start:cj_end
            filter(fi,fj) =exp((-(i-tk)^2-(j-tl)^2)/(2*sigma^2))*exp(-(pad_img(i,j)-pad_img(tk,tl))^2/(2*sigma2^2));
            fj=fj+1;
        end
            fi=fi+1;
        end
        sum_f =  sum(sum(filter));
        filter = filter/sum_f;

        conv_f_i=filter.*double(pad_img(ci_start:ci_start+filter_size-1,cj_start:cj_start+filter_size-1));
        filter_img(ci_start,cj_start) = sum(sum(conv_f_i));
    end
end

%filter = filter/sum(sum(filter));


filter_img = filter_img-1;
%s= sum(sum(filter));
%filter_img = filter;
filter_img = uint8(filter_img);

end

