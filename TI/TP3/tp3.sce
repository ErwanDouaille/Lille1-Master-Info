clear();
stacksize(100000000);
function im = quantification(img,n)
    quantificateur = 2^n
    im = img ./(256/quantificateur)
    im = im.*(256/quantificateur)
endfunction

function im = surEch(img,n)
  img = im2double(img);
  colonne = size(img,1)*n
  ligne = size(img,2)*n
  im = zeros(colonne,ligne)
  for i = 1 : colonne/n
      for j = 1 : ligne/n
          for n1 = 1 : n
              for n2 = 1 : n
                  x = i*n + n1-n
                  y = j*n + n2-n
                  im(x,y) = img(i,j);
              end
          end
      end
  end
endfunction

function im = sousEch(img,n)
    img = im2double(img);
    colonne = size(img,1)/n
    ligne = size(img,2)/n
    im = zeros(colonne,ligne)
    for i = 1 : colonne
        for j = 1 : ligne
                for n1 = 1 : n
                    for n2 = 1 : n
                        x = i*n + n1-n
                        y = j*n + n2-n
                        im(i,j) = im(i,j) + img(x,y);
                    end
                end
                im(i,j) = im(i,j) / (n*n);
        end
    end
endfunction

image = imread('ti-semaine-3-lena.png');
//imshow(image);

////////////    gris
//grisRouge = image(:,:,1);
//grisVert = image(:,:,2);
//grisBleu = image(:,:,3);
//imshow([grisRouge, grisVert, grisBleu]);

////////////    couleurs
//imageRouge = image;
//imageRouge(:,:,2) = image(:,:,1)*0;
//imageRouge(:,:,3) = image(:,:,1)*0;
//imageBleu = image;
//imageBleu(:,:,1) = image(:,:,2)*0;
//imageBleu(:,:,3) = image(:,:,2)*0;
//imageVert = image;
//imageVert(:,:,1) = image(:,:,3)*0;
//imageVert(:,:,2) = image(:,:,3)*0;
//imshow(imageRouge + imageVert + imageBleu);

//////////// sous echantillonage
imR = sousEch(image(:,:,1),2);
imR = quantification(imR, 5);

imV = im2double(image(:,:,2));

imB = sousEch(image(:,:,3),4);
imB = quantification(imB, 3);

im = zeros(size(image,1),size(image,2),size(image,3));
im (:,:,1) = surEch(imR,2);
im (:,:,2) = imV;
im (:,:,3) = surEch(imB,4);


imshow(im);

