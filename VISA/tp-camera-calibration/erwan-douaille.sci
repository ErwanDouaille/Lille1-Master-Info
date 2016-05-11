// -----------------------------------------------------------------------
/// \brief Calcule un terme de contrainte a partir d'une homographie.
///
/// \param H: matrice 3*3 définissant l'homographie.
/// \param i: premiere colonne.
/// \param j: deuxieme colonne.
/// \return vecteur definissant le terme de contrainte.
// -----------------------------------------------------------------------
function v = ZhangConstraintTerm(H, i, j)
    // vij = [hi1 hj1 , hi1 hj2 + hi2 hj1 , hi2 hj2 ,hi3 hj1 + hi1 hj3 , hi3 hj2 + hi2 hj3 , hi3 hj3 ]T 
    // Compléter la fonction ZhangConstraintTerm qui permet de calculer un vecteur ligne, équivalent à une équation de contrainte, à partir de certains coefficients d'une matrice d'homographie (début de la page 6 du rapport technique).
    // A modifier!
    v = [H(1,i)*H(1,j), (H(1,i)*H(2,j))+(H(2,i)*H(1,j)), H(2,i)*H(2,j),(H(3,i)*H(1,j))+(H(1,i)*H(3,j)), (H(3,i)*H(2,j))+(H(2,i)*H(3,j)),H(3,i)*H(3,j) ];
endfunction





// -----------------------------------------------------------------------
/// \brief Calcule deux equations de contrainte a partir d'une homographie
///
/// \param H: matrice 3*3 définissant l'homographie.
/// \return matrice 2*6 definissant les deux contraintes.
// -----------------------------------------------------------------------
function v = ZhangConstraints(H)
    v = [ZhangConstraintTerm(H, 1, 2); ...
    ZhangConstraintTerm(H, 1, 1) - ZhangConstraintTerm(H, 2, 2)];
endfunction

// -----------------------------------------------------------------------
/// \brief Calcule la matrice des parametres intrinseques.
///
/// \param b: vecteur resultant de l'optimisation de Zhang.
/// \return matrice 3*3 des parametres intrinseques.
// -----------------------------------------------------------------------
function A = IntrinsicMatrix(b)
    vo = (b(2)*b(4)-b(1)*b(5))/(b(1)*b(3)-b(2)*b(2));
    lambda = b(6)-(b(4)*b(4)+vo*(b(2)*b(4)-b(1)*b(5)))/b(1);
    alpha = sqrt(lambda/b(1));
    bet= sqrt(lambda*b(1)/(b(1)*b(3)-b(2)*b(2)));
    delta = -b(2)*alpha*alpha*bet/lambda;
    uo = (delta*vo/bet-b(4)*alpha*alpha/lambda);
    A = [alpha,lambda,uo;0,bet,vo;0,0,1];
endfunction

// -----------------------------------------------------------------------
/// \brief Calcule la matrice des parametres extrinseques.
///
/// \param iA: inverse de la matrice intrinseque.
/// \param H: matrice 3*3 definissant l'homographie.
/// \return matrice 3*4 des parametres extrinseques.
// -----------------------------------------------------------------------
function E = ExtrinsicMatrix(iA, H)
    lambda = 1/abs(iA*H(:,1));
    lambda = lambda(1);
    r1 = lambda * iA * H(:,1);
    r2 = lambda * iA * H(:,2);
    r3 = CrossProduct(r1,r2);
    t = lambda * iA * H(:,3);

    E  = [r1,r2,r3,t];
endfunction

