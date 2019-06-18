function filter_img = my_gaussian(img, filter_size, sigma)
%  img: Image.     dimension (X x Y)
%  fil_size: Filter maks(kernel)'s size.  type: (uint8)
%  sigma: Sigma value of gaussian filter.  type: (double)
pad_size = floor(filter_size/2);
pad_img = my_padding(img, pad_size, 'mirror');
[x, y] = size(img);
filter_img = zeros(x, y);

% Fill Here


index=1;

gindex=1;

center_x =pad_size+1;
center_y =pad_size+1;
mask =zeros(filter_size,filter_size);

for entire_x = 1:filter_size
  for entire_y =  1: filter_size
    mask(entire_x,entire_y) =1/(2*pi*sigma*sigma)*exp(-(entire_x-center_x)^2/(2*sigma^2)-(entire_y-center_y)^2/(2*sigma^2));
  end
end
mask =  mask/sum(sum(mask));

for i = 1:x
    for j = 1:y
      filter_img(i,j) = sum(sum(double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*mask)); 
    end
end


filter_img = uint8(filter_img);
figure, surf(mask); title('filter image');
figure, imshow(filter_img); title('image');
end