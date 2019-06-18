function pad_img = my_padding(img, pad, type)
% img: Image.     dimension (X x Y)
% pad: Pad Size.  type: (uint8)
% type: Padding type. {'mirror', 'repetition', 'zero'}
[x, y] = size(img);
pad_img = zeros(x+2*pad, y+2*pad);
pad_img(1+pad:x+pad, 1+pad:y+pad) = img;

if strcmp(type, 'mirror') % mirror padding
    up_xcounter=0;
    up_ycounter=0;
    reverse=0;
    %up_mirror
    for lxp = pad:-1:1
      if up_xcounter==x
        reverse=1;
      elseif up_xcounter==1
        reverse=0;
      end
      
      if reverse
        up_xcounter=up_xcounter-1;
      else 
        up_xcounter=up_xcounter+1;
      end
      for lyp =1:y
        pad_img(lxp,pad+lyp) = img(up_xcounter,lyp);
      end
    end
    %under_mirror
    reverse=0;
    under_xmirror = 0;
    under_xcounter=x;
    
    for lxp =x+pad+1:2*pad+x
      if under_xcounter==1
        reverse=1;
      elseif under_xcounter==x
        reverse=0;
      end
      
      if reverse
        under_xcounter=under_xcounter+1;
      else 
        under_xcounter=under_xcounter-1;
      end
      
      for lyp =1:y
        pad_img(lxp,pad+lyp) = img(under_xcounter,lyp);
      end
    end
    %left_mirror
    reverse=0;
    under_ycounter = pad;
    for lyp =pad:-1:1
      if under_ycounter==pad+2+y
        reverse=1;
      elseif under_ycounter==pad
        reverse=0;
      end
      
      if reverse
        under_ycounter=under_ycounter-1;
      else 
        under_ycounter=under_ycounter+1;
      end
        for lxp =1:2*pad+x
        pad_img(lxp,lyp) = pad_img(lxp,under_ycounter);
      end
    end
   %right_mirror
    reverse=0;
    under_ycounter = y+pad+2;
    for lyp =y+pad+2:2*pad+y
      if under_ycounter==pad
        reverse=1;
      elseif under_ycounter==y+pad+2
        reverse=0;
      end
      
      if reverse
        under_ycounter=under_ycounter+1;
      else 
        under_ycounter=under_ycounter-1;
      end
      
      
        for lxp =1:2*pad+x
        pad_img(lxp,lyp) = pad_img(lxp,under_ycounter);
      end
    end

elseif strcmp(type, 'repetition') % repetition padding
    up_xcounter=0;
    up_ycounter=0;
    for yupper = pad+1:y+pad+1
      for x_upper = pad:-1:1
        pad_img(x_upper,yupper) = pad_img(pad+1,yupper);
      end
      for x_upper = x+1+pad:2*pad+x
        pad_img(x_upper,yupper) = pad_img(x+pad,yupper);
      end
    end

    left_ycounter=pad+1;    
    for yupper = pad:-1:1
      for x_upper = pad+1:pad+x
        pad_img(x_upper,yupper) = pad_img(x_upper,left_ycounter);
      end
    end
    right_ycounter=pad+y;
    for yupper = pad+y+1:pad*2+y
      for x_upper = pad+1:pad+x+1
        pad_img(x_upper,yupper) = pad_img(x_upper,right_ycounter);
      end
    end
    
    out_left_xcounter = pad+1;
    
    for x_upper = pad:-1:1
      for yupper = pad+1:-1:1
        pad_img(x_upper,yupper) = pad_img(out_left_xcounter,pad+1);
      end
    end
    
    out_left_xcounter = pad+1+x;
    
    for x_under = pad+x+1:pad*2+x
      for yunder = pad+1:-1:1

        pad_img(x_under,yunder) = pad_img(out_left_xcounter,1+pad);
      end
    end
    for x_upper = pad:-1:1
      for yupper = pad+1:-1:1
        pad_img(x_upper,yupper) = pad_img(out_left_xcounter,pad+1);
      end
    end
    
    out_right_xcounter = pad+1+x;
    
    for x_under = pad+x+1:pad*2+x
      for yunder = pad+1+y:pad*2+y

        pad_img(x_under,yunder) = pad_img(out_left_xcounter,1+pad);
      end
    end
    
    out_right_xcounter = pad+1;
    
    for x_under = pad+1:-1:1
      for yunder = pad+1+y:pad*2+y

        pad_img(x_under,yunder) = pad_img(out_left_xcounter,1+pad+y);
      end
    end
 
    
      
    
else % zero padding
    % Free Solve Zero padding
end
pad_img = uint8(pad_img);
end