package com.techstorm.androidgame.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

import com.techstorm.androidgame.framework.FileIO;

public class AndroidFileOI implements FileIO {
	AssetManager assets;
	String externalStoragePath;
	
	public AndroidFileOI(AssetManager assets) {
		this.assets = assets;
		this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}
	
	@Override
	public InputStream readAsset(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return assets.open(fileName);
	}

	@Override
	public InputStream readFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return new FileInputStream(externalStoragePath + fileName);
	}

	@Override
	public OutputStream writeFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return new FileOutputStream(externalStoragePath + fileName);
	}
	
}
