
basse=zeros(1,40);
moyenne=zeros(1,40);
haute=zeros(1,40);
temperature=zeros(1,15);

for i=1:10
    basse(i)=1;
end
for i=11:20
    basse(i)=1-(i-10)*0.1;
end
for i=11:20
    moyenne(i)=(i-10)*0.1;
end
for i=21:30
    moyenne(i)=1-(i-20)*0.1;
end    
for i=21:30
    haute(i)=(i-20)*0.1;
end
for i=31:40
    haute(i)=1;
end
    
for i=8:10
    temperature(i)=(i-8)*0.5;
end
for i=11:15
    temperature(i)=1;
end
    
    
function exercice1()
    clf();
    plot2d(basse, style=2);
    plot2d(moyenne, style=3);
    plot2d(haute, style=5);
    plot2d(1:40, max(basse,moyenne));
endfunction

function exercice2()
    clf();
    minimum=zeros(1,40);
    maximum=zeros(1,40);
    
    minimum=minFromE(basse,moyenne);
    minimum=minFromE(minimum,haute);
    
    maximum=maxFromE(moyenne,haute);
    maximum=maxFromE(maximum,basse);
    
    plot2d(minimum, style=2);
    plot2d(maximum, style=5);
endfunction

function exercice3(temp)
    clf();
    a=zeros(1,15);
    indice=basse(temp);
    for i=1:15
        a(i)=min(temperature(i), indice);
    end    
    
    plot2d(temperature, style=5);
    plot2d(a, style=2);
endfunction

function a=minFromE(e1, e2)
    len=length(e1);
    for i=1:len
        a(i)=min(e1(i),e2(i));
    end    
endfunction

function a=maxFromE(e1, e2)
    len=length(e1);
    for i=1:len
        a(i)=max(e1(i),e2(i));
    end
endfunction

//exercice1();
//exercice2();
exercice3(12);
