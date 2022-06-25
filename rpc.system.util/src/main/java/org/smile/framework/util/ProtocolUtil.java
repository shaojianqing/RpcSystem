package org.smile.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class ProtocolUtil {
	
	public static Object transToObject(byte[] data) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(data);   
        HessianInput hessianInput = new HessianInput(is); 
        return hessianInput.readObject();
	}
	
	public static byte[] transToByte(Object object) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
        HessianOutput ho = new HessianOutput(os);  
        ho.writeObject(object);  
        return os.toByteArray();  
	}
}