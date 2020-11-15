//#version 320 es
attribute vec3 aPosition;
uniform mat4 uMVPMatrix;
//uniform vec3 uColor;
varying vec4 vColor;
void main() {
//    vColor = vec4(uColor, 1.0);
    vColor = vec4(0.0, 0.0, 1.0, 1.0);
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
}