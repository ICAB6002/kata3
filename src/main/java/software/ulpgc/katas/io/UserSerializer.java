package software.ulpgc.katas.io;

import software.ulpgc.katas.transform.TransformResult;

import java.io.OutputStream;

public interface UserSerializer {
    void write(TransformResult result, OutputStream out);
}