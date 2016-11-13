package cn.edu.jlu.ccst.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * ʹ�� pdfbox ����pdf �ĵ���Ϣ
 * 
 * @author longhuiping
 * 
 */
public class PDFParser {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static void getpdfcontent(String pdfPath, String SavePath) {		
		try {
			InputStream input = null;
			File pdfFile = new File(pdfPath);
			PDDocument document = null;
			input = new FileInputStream(pdfFile);
			document = PDDocument.load(input);
			PDFTextStripper pts = new PDFTextStripper();
			String content = pts.getText(document);
//			System.out.println("����:" + content);
			String[] lines = content.split("\n");
			String reg = "\\p{Upper}{6}S\\p{Upper}{6}";
			Pattern pattern = Pattern.compile(reg);
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(SavePath));
			for(String line : lines){
				String s = line.trim();
				Matcher matcher = pattern.matcher(s);
				if(matcher.matches()){
					bw.write(s);
					bw.write("\n");
//					System.out.println(s);
					bw.flush();
				}				
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����pdf�ĵ���Ϣ
	 * 
	 * @param pdfPath
	 *            pdf�ĵ�·��
	 * @throws Exception
	 */
	public static void pdfParse(String pdfPath, String imgSavePath) throws Exception {
		InputStream input = null;
		File pdfFile = new File(pdfPath);
		PDDocument document = null;
		try {
			input = new FileInputStream(pdfFile);
			// ���� pdf �ĵ�
			document = PDDocument.load(input);

			/** �ĵ�������Ϣ **/
			PDDocumentInformation info = document.getDocumentInformation();
			System.out.println("����:" + info.getTitle());
			System.out.println("����:" + info.getSubject());
			System.out.println("����:" + info.getAuthor());
			System.out.println("�ؼ���:" + info.getKeywords());

			System.out.println("Ӧ�ó���:" + info.getCreator());
			System.out.println("pdf ��������:" + info.getProducer());

			System.out.println("����:" + info.getTrapped());

			System.out.println("����ʱ��:" + dateFormat(info.getCreationDate()));
			System.out.println("�޸�ʱ��:" + dateFormat(info.getModificationDate()));

			// ��ȡ������Ϣ
			PDFTextStripper pts = new PDFTextStripper();
			String content = pts.getText(document);
			// System.out.println( "����:" + content );

			/** �ĵ�ҳ����Ϣ **/
			PDDocumentCatalog cata = document.getDocumentCatalog();
			List pages = cata.getAllPages();
			int count = 1;
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				if (null != page) {
					PDResources res = page.findResources();

					// ��ȡҳ��ͼƬ��Ϣ
					Map imgs = res.getImages();
					if (null != imgs) {
						Set keySet = imgs.keySet();
						Iterator it = keySet.iterator();
						while (it.hasNext()) {
							Object obj = it.next();
							PDXObjectImage img = (PDXObjectImage) imgs.get(obj);
							img.write2file(imgSavePath + count);
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != input)
				input.close();
			if (null != document)
				document.close();
		}
	}

	/**
	 * ��ȡ��ʽ�����ʱ����Ϣ
	 * 
	 * @param dar
	 *            ʱ����Ϣ
	 * @return
	 * @throws Exception
	 */
	public static String dateFormat(Calendar calendar) throws Exception {
		if (null == calendar)
			return null;
		String date = null;
		try {
			String pattern = DATE_FORMAT;
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.format(calendar.getTime());
		} catch (Exception e) {
			throw e;
		}
		return date == null ? "" : date;
	}

	public static void main(String[] args) throws Exception {
//		pdfParse("C:/Users/install/Desktop/hxs/test.pdf", "C:/Users/install/Desktop/hxs/");
		getpdfcontent("C:/Users/install/Desktop/hxs/Supp1-Pho_S.pdf", "C:/Users/install/Desktop/hxs/Supp1-Pho_S.txt");
	}
}
