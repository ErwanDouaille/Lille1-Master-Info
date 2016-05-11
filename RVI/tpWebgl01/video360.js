window.addEventListener('load',main,false);
var gl;
var vertexBuffer;
var colorBuffer;
var textureId;
var texture;
var texCoordLocation;
var programShader;

function initGL() {
	canvas=document.getElementById("webglCanvas");
	gl=canvas.getContext("experimental-webgl");
	if(!gl){
		alert("cantinitializewebglcontext");
	} else{
		console.log(gl.getParameter(gl.VERSION)+"|"+gl.getParameter(gl.VENDOR)+"|"+
			gl.getParameter(gl.RENDERER)+"|"+
			gl.getParameter(gl.SHADING_LANGUAGE_VERSION));
		gl.clearColor(0,0,0,1);
		gl.clearDepth(1.0);
		gl.enable(gl.DEPTH_TEST);
		gl.clear(gl.DEPTH_BUFFER_BIT|gl.COLOR_BUFFER_BIT);
	}
}

function getShader(id) {
	var shaderScript = document.getElementById(id);
	var k = shaderScript.firstChild;
	var str=k.textContent;
	var shader;
	if (shaderScript.type == "x-shader/x-fragment") {
		shader = gl.createShader(gl.FRAGMENT_SHADER);
 	} else if (shaderScript.type == "x-shader/x-vertex") {
		shader = gl.createShader(gl.VERTEX_SHADER);
  	}
  	gl.shaderSource(shader, str);
  	gl.compileShader(shader);

  	if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    		alert(gl.getShaderInfoLog(shader));
    		return null;
  	}
	return shader;
 }

function createProgram(id) {
	programShader=gl.createProgram();
	var vert=getShader(id+"-vs");
	var frag=getShader(id+"-fs");
	gl.attachShader(programShader,vert);
	gl.attachShader(programShader,frag);
	gl.linkProgram(programShader);
	if (!gl.getProgramParameter(programShader,gl.LINK_STATUS)) {
		alert(gl.getProgramInfoLog(programShader));
		return null;
	}
	console.log("compilation shader ok");
	return programShader;
}

function initData() {
	var vertex = [-0.5,0.5,0.0, 0.5,-0.5,0.0, -0.7,-0.9,0.0];
	vertexBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertex), gl.STATIC_DRAW);

	var color = [1.0,0.0,0.0,1.0, 0.0,1.0,0.0,1.0, 0.0,0.0,1.0,1.0];
	colorBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(color), gl.STATIC_DRAW);

	var coordLocation = [0.0,0.0, 0.7,0.5, 1.0,1.0];
	texCoordLocation = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, texCoordLocation);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(coordLocation), gl.STATIC_DRAW);

	texture = document.getElementById("image");
	textureId = gl.createTexture();
	gl.activeTexture(gl.TEXTURE0);
	gl.bindTexture(gl.TEXTURE_2D,textureId);
	gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_MIN_FILTER,gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_MAG_FILTER,gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_WRAP_S,gl.CLAMP_TO_EDGE);
	gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_WRAP_T,gl.CLAMP_TO_EDGE);
	gl.texImage2D(gl.TEXTURE_2D,0,gl.RGB,gl.RGB,gl.UNSIGNED_BYTE,texture);
}

function updateData() {

}

function drawScene() {
	gl.clear(gl.DEPTH_BUFFER_BIT | gl.COLOR_BUFFER_BIT);
	gl.useProgram(programShader);

	var vertexLocation = gl.getAttribLocation(programShader,'vertex');
	gl.enableVertexAttribArray(vertexLocation);
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.vertexAttribPointer(vertexLocation, 3, gl.FLOAT, gl.FALSE, 0, 0);

	/**
	var fragmentLocation = gl.getAttribLocation(programShader,'vColor');
	gl.enableVertexAttribArray(fragmentLocation);
	gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
	gl.vertexAttribPointer(fragmentLocation, 4	, gl.FLOAT, gl.FALSE, 0, 0);
	**/
	
	var textureLocation = gl.getUniformLocation(programShader,'texture0');
	var texCoordLocation = gl.getAttribLocation(programShader,'texCoord');
	gl.uniform1i(textureLocation, 0);
	gl.enableVertexAttribArray(texCoordLocation);
	gl.vertexAttribPointer(texCoordLocation, 2, gl.FLOAT, gl.FALSE, 0, 0);
	gl.activeTexture(gl.TEXTURE0);
	gl.bindTexture(gl.TEXTURE_2D, textureId);	

	gl.drawArrays(gl.TRIANGLES,0,3);

	gl.disableVertexAttribArray(vertexLocation);
	gl.useProgram(null);
}

function loop() {
	drawScene();
	updateData();
	window.requestAnimationFrame(loop);
}

function main() {
	initGL();
	createProgram("hello");
	initData();
	loop();
}
