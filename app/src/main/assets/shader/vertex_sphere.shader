//#version 320 es
attribute vec3 aPosition;
attribute vec3 aColor;
uniform mat4 uMVPMatrix;
varying vec4 vColor;
void main() {
    vColor = vec4(aColor, 1.0);
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
}