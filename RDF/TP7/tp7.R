library(MASS)

#Q1

(load(file="x_app.data"))
(load(file="classe_app.data"))
(load(file="classe_test.data"))
(load(file="x_test.data"))

#classe d'testrentissage
couleur <- rep('red', n_app)
couleur[classe_app==2]='green'
couleur[classe_app==3]='blue'

plot(x_app, col=couleur)


#couleur_test <- rep('red', n_test)
#couleur_test[classe_test==2]='green'
#couleur_test[classe_test==3]='blue'

#plot(x_test, col=couleur_test)


#Q2
#S <- cov(x_test)
#Vp <- eigen(S)	

# Affichage dela droite correspondant au vecteur propre
# dont la valeur propre la plus  ́lev ́e
#pente <- Vp$vectors[2,1]/Vp$vectors[1,1]
#abline(a = 0, b = pente, col = "black")

#ScalarProduct <- x_test
#ScalarProduct <- x_test %*% (Vp$vectors[,1]) / sqrt(sum(Vp$vectors[,1]*Vp$vectors[,1]))
#XP <- x_test
#XP[,1] = ScalarProduct * Vp$vectors[1,1]
#XP[,2] = ScalarProduct * Vp$vectors[2,1]
#points(XP[classe_test==1,], col="red")
#points(XP[classe_test==2,], col="green")
#points(XP[classe_test==3,], col="blue")

#Q3
x_test.qda<-qda(ScalarProduct,classe_test)
assigne_test<-predict(x_test.qda, newdata=ScalarProduct)
# Estimation des taux de bonnes classifications
table_classification_test <-table(classe_test, assigne_test$class)
# table of correct class vs. classification
diag(prop.table(table_classification_test, 1))
# total percent correct
taux_bonne_classif_test <-sum(diag(prop.table(table_classification_test)))
print(taux_bonne_classif_test)
# forme de la mclasse 1
shape<-rep(1, 301) ;
shape[assigne_test$class==2]=2
shape[assigne_test$class==3]=3

# Affichage des projections testrentissage class ́es

#plot(x_test,col=couleur,pch=shape,xlab = "X1", ylab = "X2")
#pente <- Vp$vectors[2,1]/Vp$vectors[1,1]
#abline(a = 0, b = pente, col = "black")
#XP[,1] = ScalarProduct * Vp$vectors[1,1]
#XP[,2] = ScalarProduct * Vp$vectors[2,1]
#points(XP[classe_test==1,], col="red")
#points(XP[classe_test==2,], col="green")
#points(XP[classe_test==3,], col="blue")



#Q5
# moyenne classe1
mean1 <- colMeans(x_app[classe_app==1,])
S1 <- cov(x_app[classe_app==1,])
print(S1)
# moyenne classe2
mean2 <- colMeans(x_app[classe_app==2,])
S2 <- cov(x_app[classe_app==2,])
print(S2)
# moyenne classe3
mean3 <- colMeans(x_app[classe_app==3,])
S3 <- cov(x_app[classe_app==3,])
print(S3)

mean <- (mean1 + mean2 + mean3)/3
print("Moyenne")
print(x)

Sw=S1+S2+S3
print("Somme des covariances")
print(Sw)
# covariance inter-classe
Sb=(mean1-mean)%*%t(mean1-mean)+(mean2-mean)%*%t(mean2-mean)+(mean3-mean)%*%t(mean3-mean)
print("Covariance inter classe")
print(Sb)


#Q6
# Resolution equation
invSw= solve(Sw)
invSw_by_Sb= invSw %*% Sb
Vp<- eigen(invSw_by_Sb)

# Affichage de la droite correspondant au vecteur propre
# dont la valeur propre la plus eleve
ScalarProduct <- x_app
ScalarProduct <- x_app %*% (Vp$vectors[,1]) / sqrt(sum(Vp$vectors[,1]*Vp$vectors[,1]))
pente <- Vp$vectors[2,1]/Vp$vectors[1,1]
abline(a = 0, b = pente, col = "blue")
XP[,1] = ScalarProduct * Vp$vectors[1,1]
XP[,2] = ScalarProduct * Vp$vectors[2,1]
points(XP[classe_test==1,], col="red")
points(XP[classe_test==2,], col="green")
points(XP[classe_test==3,], col="blue")


#Q7
x_app.lda<-lda(ScalarProduct,classe_app)
assigne_app<-predict(x_app.lda, newdata=ScalarProduct)
# Estimation des taux de bonnes classifications
table_classification_app <-table(classe_app, assigne_app$class)
# table of correct class vs. classification
diag(prop.table(table_classification_app, 1))
# total percent correct
taux_bonne_classif_app <-sum(diag(prop.table(table_classification_app)))
# forme de la classe 1
shape<-rep(1,n_app) ;
shape[assigne_app$class==2]=2
shape[assigne_app$class==3]=3
# Affichage des projections apprentissage classees
plot(x_app,col=couleur,pch=shape,xlab = "X1", ylab = "X2")
abline(a = 0, b = pente, col = "blue")
points(XP[classe_test==1,], col="red")
points(XP[classe_test==2,], col="green")
points(XP[classe_test==3,], col="blue")
print(taux_bonne_classif_app)

#Q8
ScalarProduct2 <- x_test
ScalarProduct2 <- x_test %*% (Vp$vectors[,1]) / sqrt(sum(Vp$vectors[,1]*Vp$vectors[,1]))

x_app.lda<-lda(ScalarProduct,classe_app)
assigne_app<-predict(x_app.lda, newdata=ScalarProduct2)
# Estimation des taux de bonnes classifications
table_classification_app <-table(classe_app, assigne_app$class)
# table of correct class vs. classification
diag(prop.table(table_classification_app, 1))
# total percent correct
taux_bonne_classif_app <-sum(diag(prop.table(table_classification_app)))
# forme de la classe 1
shape<-rep(1,n_app) ;
shape[assigne_app$class==2]=2
shape[assigne_app$class==3]=3
# Affichage des projections apprentissage classees
plot(x_app,col=couleur,pch=shape,xlab = "X1", ylab = "X2")

print(taux_bonne_classif_app)

#Q10

