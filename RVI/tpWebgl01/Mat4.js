/**
 *  basic (and lazy) matrix class
 *  F. Aubert
 */

/** ******************************************* */
function Mat4() {
	this.fv = new Float32Array(16); // major column order
}
Mat4.prototype.setIdentity = function() {
		this.fv[0]=1.0;  this.fv[4]=0.0;  this.fv[8] =0;   this.fv[12]=0.0;
		this.fv[1]=0.0;  this.fv[5]=1.0;  this.fv[9] =0.0; this.fv[13]=0.0;
		this.fv[2]=0.0;  this.fv[6]=0.0;  this.fv[10]=1.0; this.fv[14]=0.0;
		this.fv[3]=0.0;  this.fv[7]=0.0;  this.fv[11]=0.0; this.fv[15]=1.0;		
	};
Mat4.prototype.copy = function() {
		var res=new Mat4();
		for(i=0;i<16;i++) {res.fv[i]=this.fv[i];}
		return res;
	};
Mat4.prototype.setFrustum = function(left,right,bottom,top,near,far) {
		this.fv[0]=2.0*near/(right-left);   this.fv[4]=0.0;                    this.fv[8] =(right+left)/(right-left); this.fv[12]=0.0;
		this.fv[1]=0.0;                     this.fv[5]=2.0*near/(top-bottom);  this.fv[9] =(top+bottom)/(top-bottom); this.fv[13]=0.0;
		this.fv[2]=0.0;                     this.fv[6]=0.0;                    this.fv[10]=-(far+near)/(far-near);    this.fv[14]=-2.0*far*near/(far-near);
		this.fv[3]=0.0;                     this.fv[7]=0.0;                    this.fv[11]=-1.0;                      this.fv[15]=0.0;
	};
Mat4.prototype.setOrtho = function(left,right,bottom,top,near,far) {
		this.fv[0]=2.0/(right-left);   this.fv[4]=0.0;               this.fv[8] =0.0;             this.fv[12]=-(right+left)/(right-left);
		this.fv[1]=0.0;                this.fv[5]=2.0/(top-bottom);  this.fv[9] =0.0;             this.fv[13]=-(top+bottom)/(top-bottom);
		this.fv[2]=0.0;                this.fv[6]=0.0;               this.fv[10]=-2.0/(far-near); this.fv[14]=-(far+near)/(far-near);
		this.fv[3]=0.0;                this.fv[7]=0.0;               this.fv[11]=0.0;             this.fv[15]=1.0;
	};
Mat4.prototype.mult = function(a) {
		var c=this.copy();
		for(i=0;i<4;i++) {
			for(j=0;j<4;j++) {
				var res=0.0;
				for(k=0;k<4;k++) {
					res+=c.fv[i+k*4]*a.fv[k+j*4];
				}
				this.fv[i+j*4]=res;
			}
		}
	};
Mat4.prototype.setRotateX = function(angle) {
		this.fv[0]=1.0;  this.fv[4]=0.0;  			this.fv[8] =0;   			this.fv[12]=0.0;
		this.fv[1]=0.0;  this.fv[5]=Math.cos(angle);  	this.fv[9] =-Math.sin(angle); 	this.fv[13]=0.0;
		this.fv[2]=0.0;  this.fv[6]=Math.sin(angle);  	this.fv[10]=Math.cos(angle); 	this.fv[14]=0.0;
		this.fv[3]=0.0;  this.fv[7]=0.0;  			this.fv[11]=0.0; 			this.fv[15]=1.0;		
	};
Mat4.prototype.setRotateY = function(angle) {
		this.fv[0]=Math.cos(angle);		this.fv[4]=0.0;		this.fv[8] =Math.sin(angle);		this.fv[12]=0.0;
		this.fv[1]=0.0;					this.fv[5]=1.0;  	this.fv[9] =0.0;		 			this.fv[13]=0.0;
		this.fv[2]=-Math.sin(angle); 	this.fv[6]=0.0;  	this.fv[10]=Math.cos(angle); 		this.fv[14]=0.0;
		this.fv[3]=0.0;  				this.fv[7]=0.0;		this.fv[11]=0.0;					this.fv[15]=1.0;		
	};
Mat4.prototype.setTranslate = function(x,y,z) {
		this.fv[0]=1.0;		this.fv[4]=0.0;		this.fv[8] =0.0;		this.fv[12]=x;
		this.fv[1]=0.0;		this.fv[5]=1.0;  	this.fv[9] =0.0;	 	this.fv[13]=y;
		this.fv[2]=0.0; 	this.fv[6]=0.0;  	this.fv[10]=1.0;	 	this.fv[14]=z;
		this.fv[3]=0.0;  	this.fv[7]=0.0;		this.fv[11]=0.0; 		this.fv[15]=1.0;				
	};
Mat4.prototype.setScale = function(x,y,z) {
		this.fv[0]=x;		this.fv[4]=0.0;		this.fv[8] =0.0;		this.fv[12]=0;
		this.fv[1]=0.0;		this.fv[5]=y;  	this.fv[9] =0.0;	 	this.fv[13]=0;
		this.fv[2]=0.0; 	this.fv[6]=0.0;  	this.fv[10]=z;	 	this.fv[14]=0;
		this.fv[3]=0.0;  	this.fv[7]=0.0;		this.fv[11]=0.0; 		this.fv[15]=1.0;				
	};
Mat4.prototype.rotateX = function(angle) {
		var a=new Mat4();
		a.setRotateX(angle);
		this.mult(a);
	};
Mat4.prototype.rotateY = function(angle) {
		var a=new Mat4();
		a.setRotateY(angle);
		this.mult(a);
	};
Mat4.prototype.translate = function(x,y,z) {
		var a=new Mat4();
		a.setTranslate(x,y,z);
		this.mult(a);
	};
Mat4.prototype.scale = function(x,y,z) {
		var a=new Mat4();
		a.setScale(x,y,z);
		this.mult(a);
	};

