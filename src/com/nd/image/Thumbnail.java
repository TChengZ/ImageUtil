package com.nd.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;
public class Thumbnail {
	/**
	 * ��������ͼ 
	 * fromFileStr:ԭͼƬ·��
	 *  saveToFileStr:����ͼ·�� 
	 *  width:����ͼ�Ŀ� 
	 *  height:����ͼ�ĸ�
	 */
	public static void saveImageAsJpg(String fromFileStr, String saveToFileStr,
			int width, int height,boolean equalProportion) throws Exception {
		BufferedImage srcImage;
        String imgType="JPEG";
        if(fromFileStr.toLowerCase().endsWith(".png")){
        	imgType="PNG";
        }
        File fromFile=new File(fromFileStr);
        File saveFile=new File(saveToFileStr);
        srcImage=ImageIO.read(fromFile);
        if(width>0||height>0){
        	srcImage=resize(srcImage,width,height,equalProportion);
        }
        ImageIO.write(srcImage,imgType,saveFile);
	}
    
	/**
	 * ��ԭͼƬ��BufferedImage������������ͼ
	 * source��ԭͼƬ��BufferedImage����
	 * targetW:����ͼ�Ŀ�
	 * targetH:����ͼ�ĸ�
	 */
	public static BufferedImage resize(BufferedImage source,int targetW,int targetH,boolean equalProportion){
		int type=source.getType();
		BufferedImage target=null;
		double sx=(double)targetW/source.getWidth();
		double sy=(double)targetH/source.getHeight();
		//������ʵ����targetW��targetH��Χ��ʵ�ֵȱ���������
		  //�������Ҫ�ȱ����������������if else���ע�͵�����
		if(equalProportion){
			if(sx>sy){
				sx=sy;
				targetW=(int)(sx*source.getWidth());
			}else{
				sy=sx;
				targetH=(int)(sx*source.getHeight());
			}
		}
		if(type==BufferedImage.TYPE_CUSTOM){
			ColorModel cm=source.getColorModel();
			WritableRaster raster=cm.createCompatibleWritableRaster(targetW,targetH);
		    boolean alphaPremultiplied=cm.isAlphaPremultiplied();
		    target=new BufferedImage(cm,raster,alphaPremultiplied,null);
		}else{
			target=new BufferedImage(targetW,targetH,type);
			Graphics2D g=target.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source,AffineTransform.getScaleInstance(sx,sy));
			g.dispose();
		}
		return target;
	}
	
	public static void main(String[] args){
		try{
			File directory = new File("");
			System.out.println("CanonicalPath:" + directory.getCanonicalPath());//��ȡ��׼��·�� 
		    System.out.println("AbsolutePath:" + directory.getAbsolutePath());//��ȡ����·�� 
		    if(args.length > 0){
		    	File srcFile = new File(args[0]);
		    	if(srcFile.exists()){
		    		for(int i = 1; i < args.length;){
						System.out.println(args[i] + " " + args[i + 1] + " " + args[i + 2]);
						Thumbnail.saveImageAsJpg(args[0], 
					    		args[i], Integer.valueOf(args[i + 1]).intValue(), 
					    				Integer.valueOf(args[i + 2]).intValue(), true);
						i+=3;
					}
		    	}
		    }
			
		    
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
