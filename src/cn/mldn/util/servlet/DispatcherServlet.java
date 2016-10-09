package cn.mldn.util.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jspsmart.upload.SmartUpload;

import cn.mldn.util.bean.BeanValueUtil;
import cn.mldn.util.split.SplitPageUtils;
import cn.mldn.util.vaildator.Validation;

@SuppressWarnings("serial")
public abstract class DispatcherServlet extends HttpServlet {
	protected HttpServletRequest request; // ����request����
	protected HttpServletResponse response; // ����reponse����
	private ResourceBundle pageResource; // ��ȡPages.properties����
	private ResourceBundle messageResource; // ��ȡMessages.properties�Ƕ���
	private SmartUpload smart;

	@Override
	public void init() throws ServletException {
		this.pageResource = ResourceBundle.getBundle("Pages", Locale.getDefault());
		this.messageResource = ResourceBundle.getBundle("Messages", Locale.getDefault());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.request = request;
		this.response = response;
		String urlPage = "error.page";
		String status = this.getStatus();
		try {
			if (status != null) { // ��ʱ��һ��״̬�룬�����ֲ�ͬ���û�����
				String contentType = request.getContentType();
				if (contentType != null && contentType.contains("multipart/form-data")) {
					try {
						this.smart = new SmartUpload();
						this.smart.initialize(super.getServletConfig(), this.request, this.response);
						this.smart.upload();
					} catch (Exception e) {
					}
				}
				Map<String, String> errors = Validation.validate(this);
				if (errors.size() == 0) { // û�д���
					this.parameterHandle(); // �������е��ύ����
					try {
						Method statusMethod = this.getClass().getMethod(status);
						urlPage = statusMethod.invoke(this).toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else { // �ص�ָ���Ĵ���ҳ��
					request.setAttribute("errors", errors);
					urlPage = this.getClass().getSimpleName() + "." + status + ".error.page";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher(this.getPageValue(urlPage)).forward(request, response);
	}
	/**
	 * ����session��������
	 * @param name ��������
	 * @param value ��������
	 */
	public void setSessionAttribute(String name,Object value) {
		this.getSession().setAttribute(name, value);
	}
	/**
	 * ����request��������
	 * @param name ��������
	 * @param value ��������
	 */
	public void setRequestAttribute(String name,Object value) {
		this.request.setAttribute(name, value);
	}
	/**
	 * ȡ�õ�ǰ��ҵ����õĲ���
	 * 
	 * @return
	 */
	public String getStatus() {
		String uri = request.getRequestURI(); // ȡ��URI�����ݣ�/DispatcherProject/EmpServlet/edit
		String status = uri.substring(uri.lastIndexOf("/") + 1);
		return status;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	/**
	 * ȡ�õ�ǰ�û���session����
	 * @return
	 */
	public HttpSession getSession() {
		return this.request.getSession() ;
	}

	/**
	 * ȡ��ָ��������Ӧ�����ݣ������ı��Ƿ񱻷�װ
	 * 
	 * @param paramName
	 * @return
	 */
	public String getStringParameter(String paramName) {
		String contentType = request.getContentType();
		if (contentType != null && contentType.contains("multipart/form-data")) {
			return this.smart.getRequest().getParameter(paramName);
		} else {
			return this.request.getParameter(paramName);
		}
	}

	/**
	 * ȡ��ָ���������ҽ����Ϊint�����ݷ���
	 * 
	 * @param paramName
	 * @return
	 */
	public int getIntParameter(String paramName) {
		return Integer.parseInt(this.getStringParameter(paramName));
	}

	/**
	 * ȡ��ָ���������ҽ����Ϊint�����ݷ���
	 * 
	 * @param paramName
	 * @return
	 */
	public double getDoubleParameter(String paramName) {
		return Double.parseDouble(this.getStringParameter(paramName));
	}

	/**
	 * ȡ��ָ���������ҽ����ΪDate�����ݷ���
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDateParameter(String paramName) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(this.getStringParameter(paramName));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȡ��ָ���������ҽ����ΪDate����
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDatetimeParameter(String paramName) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getStringParameter(paramName));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ʵ���������������VO��ת���Ĵ������
	 */
	private void parameterHandle() {
		String contentType = request.getContentType();
		if (contentType != null && contentType.contains("multipart/form-data")) { // ����װ
			// 1������SmartUpload����ʵ��������
			try {
				Enumeration<String> enu = this.smart.getRequest().getParameterNames();
				while (enu.hasMoreElements()) { // ȡ��ȫ���Ĳ�������
					String paramName = enu.nextElement();
					String paramValue = this.smart.getRequest().getParameter(paramName);
					new BeanValueUtil(this, paramName, paramValue).setObjectValue();
				}
			} catch (Exception e) {
			}
		} else {
			// 1����Ҫȡ��ȫ���������������
			Enumeration<String> enu = this.request.getParameterNames();
			while (enu.hasMoreElements()) { // ȡ��ȫ���Ĳ�������
				String paramName = enu.nextElement();
				String paramValue = this.request.getParameter(paramName);
				new BeanValueUtil(this, paramName, paramValue).setObjectValue();
			}
		}
	}

	/**
	 * ����ҵ��������֮�����ת·������ʾ��Ϣ��Key
	 * 
	 * @param pageKey
	 *            ��Ӧ��Pages.properties�ļ���ָ����key��Ϣ
	 * @param messageKey
	 *            ��Ӧ��Messages.properties�ļ���ָ����key��Ϣ
	 */ 
	public void setUrlAndMsg(String pageKey, String messageKey) {
		this.request.setAttribute("url", this.getPageValue(pageKey));
		System.out.println("######### " + this.getMessageValue(messageKey));
		if (this.getType() == null || "".equals(this.getType())) {
			this.request.setAttribute("msg", this.getMessageValue(messageKey));
		} else { // ��֮ǰ���õ�ÿһ��Servlet�е�ռλ���������滻
			this.request.setAttribute("msg", MessageFormat.format(this.getMessageValue(messageKey), this.getType()));
		}
	}

	/**
	 * ȡ��Pages.properties�ļ���ָ����key��Ӧ��value����
	 * 
	 * @param pageKey
	 *            Ҫ��ȡ����Դ�ļ���key��Ϣ
	 * @return
	 */
	public String getPageValue(String pageKey) {
		try {
			return this.pageResource.getString(pageKey) ;
		} catch (Exception e) {}
		return this.pageResource.getString("error.page") ;
	}
	/**
	 * �����µ��ϴ��ļ�����
	 * @return
	 */
	public String createSingleFileName() {
		return UUID.randomUUID() + "." + this.smart.getFiles().getFile(0).getFileExt() ;
	}
	/**
	 * �����ļ��ı��洦��
	 */
	public boolean saveUploadFile(String fileName) {
		try {
			if (this.getUploadDir() == null || "".equals(this.getUploadDir())) {
				this.smart.getFiles().getFile(0).saveAs(super.getServletContext().getRealPath("/") + fileName);
			} else {
				String filePath = super.getServletContext().getRealPath("/") + this.getUploadDir() + fileName ;
				File file  = new File(filePath) ;
				if (!file.getParentFile().exists()) {	// ����Ŀ¼������
					file.getParentFile().mkdirs() ;
				}
				this.smart.getFiles().getFile(0).saveAs(filePath); 
			}
			return true ;
		} catch (Exception e) {
			return false ;
		} 
	}
	/**
	 * �ж��Ƿ����ϴ��ļ�
	 * @return
	 */
	public boolean isUploadFile() {	// �Ƿ�������ϴ��ļ�
		if (this.smart == null) {
			return false ;
		}
		try {
			if (this.smart.getFiles().getSize() > 0) {
				return true ;
			}
		} catch (IOException e) {
			return false ; 
		}
		return false ; 
	}

	/**
	 * ȡ��Messages.properties�ļ���ָ����key��Ӧ��value����
	 * 
	 * @param messageKey
	 *            Ҫ��ȡ����Դ�ļ���key��Ϣ
	 * @return
	 */
	public String getMessageValue(String messageKey) {
		return this.messageResource.getString(messageKey);
	}
	/**
	 * ����ҳ����Ҫ�Ĳ�����ʹ��request���Դ���
	 * @param urlKey ��ҳִ��Ҫʹ�õ�url
	 * @param allRecorders �ܵļ�¼��
	 * @param spu ��ҳ����ز���
	 */
	public void setSplitPage(String urlKey,int allRecorders,SplitPageUtils spu) {
		this.request.setAttribute("url", this.getPageValue(urlKey));
		this.request.setAttribute("allRecorders", allRecorders);
		this.request.setAttribute("currentPage", spu.getCurrentPage()); 
		this.request.setAttribute("lineSize", spu.getLineSize()); 
		this.request.setAttribute("column", spu.getColumn()); 
		this.request.setAttribute("keyWord", spu.getKeyWord());  
		this.request.setAttribute("columnData", this.getDefaultColumn());  
	}
	/**
	 * �������з�ҳ�ĺ�ѡ�У���ʽ�����ա���ǩ:������|��ǩ:������|��
	 * @return
	 */
	public abstract String getDefaultColumn() ;
	/**
	 * ȡ���ϴ��ļ�����Ŀ¼
	 * @return
	 */
	public abstract String getUploadDir() ;

	/**
	 * ȡ��ÿһ������ľ���Ĳ������ͣ�����Ϊ��Ϣ�����ʹ��
	 * 
	 * @return ����ÿһ��ģ�������
	 */
	public abstract String getType();
}
