package com.kevin.rpc.register.remote;

import com.kevin.rpc.api.entity.RrcUrl;
import com.kevin.rpc.register.RemoteRegister;

import java.io.*;
import java.util.*;

public class RemoterMapRegister implements RemoteRegister {
    private Map<String, List<RrcUrl>> registerMap = new HashMap<String, List<RrcUrl>>(1024);
    public static final String path = "/data/register";

    public void register(String interfaceName, RrcUrl host) {
        if (registerMap.containsKey(interfaceName)) {
            List<RrcUrl> list = registerMap.get(interfaceName);
            list.add(host);
        } else {
            List<RrcUrl> list = new LinkedList<RrcUrl>();
            list.add(host);
            registerMap.put(interfaceName, list);
        }
        try {
            saveFile(path, registerMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RrcUrl getRadomURL(String interfaceName) {
        try {
            registerMap = (Map<String, List<RrcUrl>>) readFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<RrcUrl> list = registerMap.get(interfaceName);
        Random random = new Random();
        int i = random.nextInt(list.size());
        return list.get(i);
    }

    /**
     * 写入文件
     *
     * @param path
     * @param object
     * @throws IOException
     */
    private void saveFile(String path, Object object) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
    }

    /**
     * 从文件中读取
     *
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object readFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
        return inputStream.readObject();
    }
}
