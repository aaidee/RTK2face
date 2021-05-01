import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import java.io.*;

public class CanvasRepaint {

	/**
	 * �� ���α׷��� License�� AGPL3 http://www.gnu.org/licenses/agpl-3.0.html
	 * ���������� ������ ����� �� �ڵ��Դϴ�. �ڵ尡 �������ϰ� �ҿ����ϹǷ� �˾Ƽ� ��ġ�ñ� �ٶ��ϴ�.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final rtk2face face= new rtk2face("kaodata.dat", 40);
		
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setText("�ﱹ��");
	    shell.setLayout(new FillLayout());

	    Canvas canvas = new Canvas(shell, SWT.NONE);
	    
	    final int xx=20;
	    final int yy=12;
	    
	    PaletteData palette = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	    ImageData sourceData = new ImageData(64, face.getHeight(), 24, palette);
	    ImageData sourceData2 = new ImageData(64*xx, face.getHeight()*yy, 24, palette);
	    sourceData.setPixels(0, 0, 64*face.getHeight()-1, face.getFace(200), 0);
	    sourceData2.setPixels(0, 0, 64*xx * face.getHeight()*yy -1, face.getFaces(0,xx*yy-1,xx,yy), 0);
	    //face.getFaces(0, 5, 3, 2);

	    final Image source = new Image(display, sourceData);
	    final Image source2 = new Image(display, sourceData2);

	    canvas.addPaintListener(new PaintListener() {
	    	public void paintControl(PaintEvent e) {
	    		e.gc.drawImage(source2, 0, 0, 64*xx, face.getHeight()*yy, 0, 0, 64*xx, face.getHeight()*yy*2);
	    		//e.gc.drawImage(source, 0, 0, 64, face.getHeight(), 100, 100, 64, face.getHeight()*2);
	    	}
	    });

	    canvas.redraw();
	    
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();
	}
}

class rtk2face {
	private String filePath;
	private int height;

	public rtk2face(String filePath, int height){
		this.filePath = filePath;
		this.height = height;
	}
	
	public int[] getFace(int faceIndex){
		byte[] srcByte = new byte[24 * height]; // srcByte: ���� ����Ʈ �迭
		int[] src = new int[24 * height]; // src: ���� int �迭
		int[] out = new int[64 * height]; // out: ��� int �迭
		
		try{
			RandomAccessFile rf = new RandomAccessFile(filePath, "r");
			rf.seek(960*faceIndex); // ���� ù ��ġ��
			//rf.seek(0x780+960*216);
			//a = rf.readLine();
			rf.read(srcByte, 0, 24*height); // �� �ϳ� ����
			// srcByte�� src�� �ٲ۴�
			for ( int i = 0; i < srcByte.length; i++){
				src[i] = srcByte[i] & 0xff;
				//System.out.printf("%x ", src[i] );
			}
			System.out.printf("\n");
			
					/*�� ����Ʈ�� �а� �����ȼ��� �Ķ����� �ű��
					 * ��ǥ�� �����͸� -8�Ѵ�
					 * �� ����Ʈ�� �а� �����ȼ��� �������� �ű��
					 * ��ǥ�� �����͸� -8�Ѵ�
					 * �� ����Ʈ�� �а� �����ȼ��� ����� �ű��*/
					int x = 0;
					int k = 0;
					// 00000000, 11111111
					while( k < src.length){
						out[x++] = ((src[k] & 128) >> 7) * 0x0000ff;
						out[x++] = ((src[k] & 64) >> 6)* 0x0000ff;
						out[x++] = ((src[k] & 32) >> 5)* 0x0000ff;
						out[x++] = ((src[k] & 16) >> 4)* 0x0000ff;
						out[x++] = ((src[k] & 8) >> 3)* 0x0000ff;
						out[x++] = ((src[k] & 4) >> 2)* 0x0000ff;
						out[x++] = ((src[k] & 2) >> 1)* 0x0000ff;
						out[x++] = ((src[k++] & 1))* 0x0000ff;
						x -= 8;
						out[x++] |= ((src[k] & 128) >> 7) * 0xff0000;
						out[x++] |= ((src[k] & 64) >> 6)* 0xff0000;
						out[x++] |= ((src[k] & 32) >> 5)* 0xff0000;
						out[x++] |= ((src[k] & 16) >> 4)* 0xff0000;
						out[x++] |= ((src[k] & 8) >> 3)* 0xff0000;
						out[x++] |= ((src[k] & 4) >> 2)* 0xff0000;
						out[x++] |= ((src[k] & 2) >> 1)* 0xff0000;
						out[x++] |= ((src[k++] & 1))* 0xff0000;
						x -= 8;
						out[x++] |= ((src[k] & 128) >> 7) * 0x00ff00;
						out[x++] |= ((src[k] & 64) >> 6)* 0x00ff00;
						out[x++] |= ((src[k] & 32) >> 5)* 0x00ff00;
						out[x++] |= ((src[k] & 16) >> 4)* 0x00ff00;
						out[x++] |= ((src[k] & 8) >> 3)* 0x00ff00;
						out[x++] |= ((src[k] & 4) >> 2)* 0x00ff00;
						out[x++] |= ((src[k] & 2) >> 1)* 0x00ff00;
						out[x++] |= ((src[k++] & 1))* 0x00ff00;
					}
					for(int j=0; j<out.length; j++){
						if(out[j] == 0x000000)//��
							out[j] = 0x000000;
						if(out[j] == 0xff0000)//��
							out[j] = 0xff5555;
						if(out[j] == 0xffff00)//��
							out[j] = 0xffff55;
						if(out[j] == 0x00ff00)//��
							out[j] = 0x55ff55;
						if(out[j] == 0x00ffff)//��
							out[j] = 0x55ffff;
						if(out[j] == 0x0000ff)//��
							out[j] = 0x5555ff;
						if(out[j] == 0xff00ff)//��
							out[j] = 0xff55ff;
						if(out[j] == 0xffffff)//��
							out[j] = 0xffffff;
					}
					for ( int i = 0; i < out.length; i++){
						//System.out.printf("%x ", out[i]);
					}
					for ( int i = 0; i < srcByte.length; i++){
						src[i] = srcByte[i] & 0xff;
						//System.out.printf("%x ", src[i] );
					}
		}catch(FileNotFoundException e){
			System.out.println("�ش� ���丮�� ������ �����ϴ�.");
		}catch(IOException e){
			System.out.println("������ ������ �����ϴ�.");
		}catch(Exception e){
			System.out.println("�˼� ���� �����Դϴ�.");
		}
		return out;
	}
	
	public int[] getFaceLine(int faceIndex, int lineIndex){
		int[] out = new int[64];
		int[] temp = new int[64*this.height];
		temp = getFace(faceIndex);
		for(int i=0; i<64; i++){
			out[i] = temp[lineIndex*64+i];
		}		
		return out;
	}
	
	public int[] getFaces(int faceIndexFrom, int faceIndexTo, int x, int y){
		int[] out = new int[x*64 * y*this.height];
		//System.out.println(x*64 * y*this.height);
		int k = 0;
		int firstX = 0;
		while (k<out.length){
			for(int j=0; j<this.height; j++){
				for(int i=firstX; i<firstX+x; i++){
					int[] temp = new int[64];
					temp = getFaceLine(faceIndexFrom+i, j);
					for(int t=0; t<temp.length; t++){
						out[k++] = temp[t];
					}
					//System.out.println((k-1)+"!"+faceIndexFrom+i+"!"+j);
				}
			}
			firstX += x;
		}
		return out;
	}
	
	public int getHeight(){
		return height;
	}
}
