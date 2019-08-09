//使用外部纹理必须支持此扩展
#extension GL_OES_EGL_image_external : require

precision mediump float;
uniform samplerExternalOES inputImageOESTexture;
varying vec2 textureCoordinate;
void main()
{
    gl_FragColor = texture2D(inputImageOESTexture, textureCoordinate);
}