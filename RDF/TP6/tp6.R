library(MASS)

#Q1
(load(file="simul1.Rdata"))

#classe d'apprentissage
couleur <- rep('red', n_app)
couleur[classe_app==2]='blue'
couleur[classe_app==3]='green'

plot(x_app, col=couleur)

# classe de test
couleur_test <- rep('red', n_test)
couleur_test[classe_test==2]='blue'
couleur_test[classe_test==3]='green'

plot(x_test, col=couleur_test)

# Q2
print("M")
print(mean(x_app[classe_app==1,1]))
print(mean(x_app[classe_app==1,2]))
print(mean(x_app[classe_app==2,1]))
print(mean(x_app[classe_app==2,2]))
print(mean(x_app[classe_app==3,1]))
print(mean(x_app[classe_app==3,2]))

print("Sigma")
print(sqrt(cov(as.vector(x_app[classe_app==1,1]),as.vector(x_app[classe_app==1,1]))))
print(sqrt(cov(as.vector(x_app[classe_app==1,2]),as.vector(x_app[classe_app==1,2]))))
print(sqrt(cov(as.vector(x_app[classe_app==2,1]),as.vector(x_app[classe_app==2,1]))))
print(sqrt(cov(as.vector(x_app[classe_app==2,2]),as.vector(x_app[classe_app==2,2]))))
print(sqrt(cov(as.vector(x_app[classe_app==3,1]),as.vector(x_app[classe_app==3,1]))))
print(sqrt(cov(as.vector(x_app[classe_app==3,2]),as.vector(x_app[classe_app==3,2]))))

# Q3

x_app.qda<-qda(x_app,classe_app)
Zp<-predict(x_app.qda,grille)
assigne_test<-predict(x_app.qda, newdata=x_test)
	

# Estimation des taux de bonnes classifications
table_classification_test <-table(classe_test, assigne_test$class)
# table of correct class vs. classification
diag(prop.table(table_classification_test, 1))
# total percent correct
taux_bonne_classif_test <-sum(diag(prop.table(table_classification_test)))
print("Taux bonne classif test")
print(taux_bonne_classif_test)

# forme de la classe 1
shape<-rep(1,n_test) ;
# forme de la classe 2
shape[assigne_test$class==2]=2 ;
# forme de la classe 3
shape[assigne_test$class==3]=3 ;

plot(x_test,col=couleur,pch=shape,xlab = "X1", ylab = "X2")

xp1<-seq(min(x_app[,1]),max(x_app[,1]),length=50)
xp2<-seq(min(x_app[,2]),max(x_app[,2]),length=50)
grille<-expand.grid(x1=xp1,x2=xp2)
grille=cbind(grille[,1],grille[,2])
Zp<-predict(x_app.qda,grille)
zp<-Zp$post[,3]-pmax(Zp$post[,2],Zp$post[,1])
contour(xp1,xp2,matrix(zp,50),add=TRUE,levels=0,drawlabels=FALSE)

#Q5
print("Q5 !!!!")
(load(file="Iris_x2_x4.Rdata"))

x_test.qda<-qda(x_test,classe_test)
Zp<-predict(x_test.qda,grille)
assigne_test<-predict(x_test.qda, newdata=x_app)
	

# Estimation des taux de bonnes classifications
table_classification_test <-table(classe_test, assigne_test$class)
# table of correct class vs. classification
diag(prop.table(table_classification_test, 1))
# total percent correct
taux_bonne_classif_test <-sum(diag(prop.table(table_classification_test)))
print("Taux bonne classif test")
print(taux_bonne_classif_test)

# forme de la classe 1
shape<-rep(1,n_app) ;
# forme de la classe 2
shape[assigne_test$class==2]=2 ;
# forme de la classe 3
shape[assigne_test$class==3]=3 ;

plot(x_test,col=couleur,pch=shape,xlab = "X1", ylab = "X2")

xp1<-seq(min(x_test[,1]),max(x_test[,1]),length=50)
xp2<-seq(min(x_test[,2]),max(x_test[,2]),length=50)
grille<-expand.grid(x1=xp1,x2=xp2)
grille=cbind(grille[,1],grille[,2])
Zp<-predict(x_test.qda,grille)
zp<-Zp$post[,3]-pmax(Zp$post[,2],Zp$post[,1])
contour(xp1,xp2,matrix(zp,50),add=TRUE,levels=0,drawlabels=FALSE)

