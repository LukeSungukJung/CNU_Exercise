function filter_img = my_filter(img, filter_size, type)
%  img: Image.     dimension (X x Y)
%  fil_size: Filter maks(kernel)'s size.  type: (uint8)
%  type: Filter type. {'avr', 'laplacian', 'median', 'sobel', 'unsharp'}
pad_size = floor(filter_size/2);
pad_img = my_padding(img, pad_size, 'mirror');
[x, y] = size(img);
filter_img = zeros(x, y);

if strcmp(type, 'avr')
    for i = 1:x
        for j = 1:y
            % fil_size-1까지로 해야 filter size만큼의 mask가 잡힘
            filter_img(i,j) = mean(mean(pad_img(i:i+filter_size-1, j:j+filter_size-1)));
        end
    end
elseif strcmp(type, 'weight')
    % 다른 필터의 마스크를 만들 때에 참고
    mask = [1:pad_size+1 pad_size:-1:1]' * [1:pad_size+1 pad_size:-1:1];
    % 필터의 합이 1이 되게 하기 위해 sum을 미리 구함
    s = sum(sum(mask));
    for i = 1:x
       for j = 1:y
           filter_img(i,j) = sum(sum(double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*mask))/s; 
       end
    end
elseif strcmp(type, 'laplacian') % Laplacian Filter
     laple_masks= -1*ones(filter_size,filter_size);
     center_x = pad_size+1;
     center_y = pad_size+1;
     coeff =0;
     base = filter_size*filter_size;
     cx = 0;
     cy = 0;
     laple_masks(center_x,center_y) = base-1;

     for i = 1:x
       for j = 1:y
           filter_img(i,j) = sum(sum((double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*laple_masks))); 
       end
    end

    % Fill here
elseif strcmp(type, 'median') % Median Filter
    x_count_stack = 1;
    real_x = pad_size+1;
    real_y = pad_size+1;
    for pad_img_x= 1:x+pad_size
      for pad_img_y = 1:y +pad_size
        index = 1;
        list_value = [];
        add_val = 1;
        for fx = pad_img_x:pad_img_x+filter_size
           if fx>x+pad_size
            add_val =0;
            break;
           end
           for fy =pad_img_y:pad_img_y+filter_size
             if fy>y+pad_size
              add_val =0;
              break;
             end
             list_value(index) = pad_img(fx,fy);
             list_value;
             pad_img(fx,fy);
             index=index+1;
             index;
           end
         end
         if add_val ==1
         m_value = median(list_value);
         filter_img(real_x,real_y) =m_value;
         end
       
      real_y=real_y+1;
    end
      real_y=pad_size+1;
      %x_count_stack=x_count_stack+1
      real_x=real_x+1;
    end
    % Fill here
    
elseif strcmp(type, 'sobel') % Sobel Filter
    basic_filter_size = 3;
    long_sx = 0;
    % Fill it
    v_img = zeros(x, y);
    h_img = zeros(x, y);

      long_sx = zeros(filter_size,filter_size);
      center_x = pad_size+1;
      center_y = pad_size+1;
      for lsix = 1:pad_size
        for lsiy =1:filter_size
          if lsiy == center_y
            long_sx(lsix,lsiy) = -2;
          else
            long_sx(lsix,lsiy) = -1;
          end
         end 
    %  end
          under_sx = -1*flip(long_sx(1:center_x-1,1:filter_size));
          long_sx(center_x+1:filter_size,1:filter_size) = under_sx;
          long_sy = long_sx';
  
        for i = 1:x
       for j = 1:y
           v_img(i,j) = sum(sum(double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*long_sx));
           h_img(i,j) = sum(sum(double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*long_sy));   
       end
      end
  
  end
    % Fill here
    filter_img = h_img+v_img;
    % Sobel에서 만들어지는 이미지 비교
    figure
    subplot(1, 1500, 1:490), imshow(v_img), title('v mask image');
    subplot(1, 1500, 501:990), imshow(h_img), title('h mask image');
    subplot(1, 1500, 1001:1490), imshow(filter_img), title('sobel image');
elseif strcmp(type, 'unsharp')
    k = 0.5;
    l_filter = zeros(filter_size,filter_size);
    l_filter(pad_size+1,pad_size+1) = 1;
    l_filter =l_filter*(1/(1-k));
    h_filter = -1*ones(filter_size,filter_size)/(filter_size*filter_size);
    h_filter = h_filter*(k/(1-k));
    
    for i = 1:x
       for j = 1:y
           filter_img(i,j) = sum(sum(double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*l_filter+double(pad_img(i:i+filter_size-1,j:j+filter_size-1)).*h_filter)); 
       end
    end
    
    % k는 조정해도 괜찮음 (0 <= k <= 1)

    % Fill it
end
filter_img = uint8(filter_img);
end