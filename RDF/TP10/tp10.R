library ("EBImage")

rdfReadGreyImage <- function (nom) {
 image <- readImage (nom)
 if (length (dim (image)) == 2) {
   image
 } else {
   channel (image, 'red')
 }
}


calculEntropie <- function(serie)
{
	entropie = matrix(rep(0,40*33),nrow=33,ncol=40);
	res = 0;
	for (i in 1:33) {
		for (j in 1:40) {
			for (k in 1:400) {
				res = res +serie[i,j,k];
			}
		}
		m = res /400;
		entropie[i,j] <- log2((m)^(m)) - log2((1-m)^(1-m));
	}
	entropie;
}

allFacesName = "rdf-allFaces.png";
imageAllFaces = rdfReadGreyImage(allFacesName);

#range les visages dans un tableau
stackedFaces <- array(0, dim=c(33,40,400));
for (i in 0:19) 
{
	for (j in 0:19) 
	{
		stackedFaces[,,(i*20+j+1)] = imageAllFaces[(1+j*33) : ((j+1)*33), (1+i*40) : ((i+1)*40)]; 	
	}
}
