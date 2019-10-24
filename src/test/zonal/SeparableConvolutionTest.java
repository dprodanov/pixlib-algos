package test.zonal;
import ij.ImageJ;
import ij.ImagePlus;
import ijaux.*;
import ijaux.datatype.UnsupportedTypeException;
import dsp.BCFactory;
import dsp.BCTypes;
import dsp.BCFactory.MirrorCondition;
import ijaux.hypergeom.*;
import ijaux.hypergeom.convolution.Convolver;
import ijaux.hypergeom.convolution.SeparableKernel;
import ijaux.hypergeom.index.BaseIndex;


public class SeparableConvolutionTest implements Constants {

	PixLib plib=new PixLib();
	
	@SuppressWarnings("unchecked")
	public SeparableConvolutionTest() {
	 	
		int[] dim={100, 100};
		//int ndim=dim.length;
		final int size=Util.cumprod(dim);
	 
		byte[] pixels_byte=   Util.randByte(size);
		float[] pixels_float=   Util.randFloat(size);
		
		Byte b=new Byte((byte)0);
		
		Float f=new Float(0f);
		
		PixelCube<Float,BaseIndex> cube=new PixelCube<Float,BaseIndex>(dim, pixels_float, f);
		cube.setIndexing(BASE_INDEXING);
		//int[] dimx={4, 2};
	 		
		float [][] akern2={{1f, -1f,  1f, 1f}, 
						   {1f, -1f, 1f}};
		int[][] dims={{4},{3}};
		//int [] ord={0,1};
		SeparableKernel<Float, float[]> sc= new SeparableKernel<Float, float[]>(akern2, f,  dims);	
		
		
		byte [][] akern4={{1, -1,  1, 1}, 
				   {1, -1, 1}};

		//int [] ord={0,1};
		SeparableKernel<Byte, byte[]> sc2= new SeparableKernel<Byte, byte[]>(akern4, b,  dims);	
			 
		try {
			ImagePlus imp4 = null;
			imp4 = plib.imageFrom("test float",cube);
			imp4.show();

		 Region<Float> reg=new Region<Float>(cube,sc);
		 // System.out.println("Region:");
		// System.out.println(reg);
		 
		 
		 
	
		 Convolver<Float> mproc=new Convolver<Float>(sc);
		
		 
		 @SuppressWarnings("rawtypes")
		 MirrorCondition bc= (MirrorCondition) BCFactory.create(BCTypes.MIRROR,  reg.getIndex());
			
		 //TranslatedCondition bc= reg.new TranslatedCondition(reg.getIndex());
		 //StaticCondition bc= reg.new StaticCondition(reg.getIndex());
		 mproc.setBoundaryCondition(bc);
		// Region<Float> regout=mproc.convolve(reg);
		
		// System.out.println(regout);
		  Region<Float> regout2=mproc.convolveSeparable(reg,sc);
		 
		 ImagePlus imp3=null;
	
			imp3 = plib.imageFrom("convolved byte",regout2);

		
		 
			PixelCube<Byte,BaseIndex> cube_b=new PixelCube<Byte,BaseIndex>(dim, pixels_byte, b);
			cube_b.setIndexing(BASE_INDEXING);
			
			 Region<Byte> reg2=new Region<Byte>(cube_b,sc);
			 // System.out.println("Region:");
			// System.out.println(reg);
			 
			 MirrorCondition bc2= (MirrorCondition) BCFactory.create(BCTypes.MIRROR,  reg.getIndex());
				
			 //TranslatedCondition bc= reg.new TranslatedCondition(reg.getIndex());
			 //StaticCondition bc= reg.new StaticCondition(reg.getIndex());
			 mproc.setBoundaryCondition(bc2);
			// Region<Float> regout=mproc.convolve(reg);
			
			// System.out.println(regout);
			  Region<Byte> regout3=mproc.convolveSeparable(reg2,sc2);
			 
			 
		
				imp4 = plib.imageFrom("convolved byte",regout3);
				 imp4.show();
			} catch (UnsupportedTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 
			 byte[] bp=sc2.join();
			 
			 PixelCube<Byte,BaseIndex> sc_cube=new PixelCube<Byte,BaseIndex>(sc.getDimensions(), bp, b);
				cube.setIndexing(BASE_INDEXING);
				
			 ImagePlus imp5 = null;
			 try {
				 
					imp5 = plib.imageFrom("kernel",sc_cube);
				} catch (UnsupportedTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 imp5.show();
		 
	}

	private void printdim(int [] dim) {
		System.out.print("[ ");
		for (int d: dim) {
			System.out.print(d+" ");
		}
		System.out.println("]");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.setProperty("plugins.dir", args[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ImageJ();
		new SeparableConvolutionTest();

	}

}
