library(MASS)
library(mclust)

#Q1
(load(file="iris-tp8.RData"))
couleur <- rep('red', n)
couleur[classe==2]='green'
couleur[classe==3]='blue'

# ACP
S <- cov(x_aff)
Vp <- eigen(S)  

# Affichage dela droite correspondant au vecteur propre
pente <- Vp$vectors[2,1]/Vp$vectors[1,1]

ScalarProduct <- x_aff %*% (Vp$vectors[,1]) / sqrt(sum(Vp$vectors[,1]*Vp$vectors[,1]));
XP <- x_aff
XP[,1] = ScalarProduct * Vp$vectors[1,1]
XP[,2] = ScalarProduct * Vp$vectors[2,1]



centre_init <- t(matrix(c(XP[1,],XP[51,],XP[101,]),2,3));
km5 <- kmeans(XP, 3, 15, centers = centre_init)
print(XP)
# plot centers
centers_aff <- cbind(km5$centers[,1],km5$centers[,2])
shape<-rep(1, n) 
shape[classe==2]=2
shape[classe==3]=3
print("Taux erreur:")
print(classError(km5$cluster, classe))

plot(x_aff, col=couleur,pch=shape, xlim=c(-1,5), ylim=c(-2,3))
abline(a = 0, b = pente, col = "black")
points(XP[classe==1,], col="red")
points(XP[classe==2,], col="green")
points(XP[classe==3,], col="blue")
points(centers_aff, col = 'black', pch = 8)
