package jarnal;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.lang.Math.*;
import java.lang.Number.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;

import jarnal.Jpages;
import jarnal.Jtool;
import jarnal.Jpar;
import jarnal.Jcom;
import jarnal.Janalyze;
import jarnal.Jarnbox;
import jarnal.Jarnout;
import jarnal.Jsendmail;

public class Jarnal extends JApplet{

    static Dimension frameSize = new Dimension(738, 992);
    static Point frameLocation = new Point(0, 0);
    static int nWins = 0;

    static String ext = ".jaj";	
    static String openfile = "";
    static String openbgfile = "";
    static String openmfile = "";
    static String opentextfile = "";
    static String savefile="";
    static String pdffile="";
    static String confdir="";
    static String meta = "";
    static String meta2 = "";
    static boolean javagui = false;
    static boolean doneMeta = false;
    static LinkedList loadFiles = new LinkedList();
    static boolean setLocation = false;
    static boolean locationSet = false;
    static boolean showMenu = true;
    static boolean template = false;
    static HashSet wins = new HashSet();
    static boolean isApplet = false;
    static boolean showGUI = false;
    static boolean startfs = false;
    public static String nextScrap = null;
    public static Transferable internalClipboard;
    boolean embed = false;
    static public boolean miniDic = false;
    static public String defaultConf;
    static public String memoryerrorstring = "";

    static String jarnalshell ="pdfrenderer=pdftoppm -f %1 -l %2 -r %4 %5 %3\nps2pdf=ps2pdf %1 %2\nbrowser=firefox %1\nprintpdf=lpr %1\npdftotext=pdftotext -eol unix -layout -f %1 -l %2 %3 %4\npdfconverter=soffice";
    static String jarnalshellwin ="pdfrenderer=C:/gs/gs8.50/bin/gswin32c -dNOPAUSE -dBATCH -d  -dGraphicsAlphaBits=4 -dTextAlphaBits=4 -dFirstPage=%1 -dLastPage=%2  -sDEVICE=png16m -sOutputFile=%3 -r%4 -f \"%5\"\nps2pdf=ps2pdf %1 %2\nbrowser=\"c:\\program files\\internet explorer\\iexplore.exe\" %1\nprintpdf=lpr %1\npdftotext=basepathlib -eol unix -layout -f %1 -l %2 %3 %4\npdfconverter=soffice";
    static String firefox;
    static String ps2pdf;
    static String gs;
    static String printpdf;
    static String pdfconverter;
    static int tzadjust = 0;
    static boolean printaround = false;

    static String homeserver="http://www.dklevine.com/general/software/tc1000/";
    static String startconnect = null;

    static String language=Locale.getDefault().toString().substring(0, 2);
    static Hashtable hlang = null;

    JrnlPane jrnlPane;
    Jarnout outline;
    JScrollPane sp;
    JLabel statusBar;
    JFrame gJrnlFrame;
    Toolkit toolkit;

    public String templateFile = null;
    public String fname = "";
    public String internalName = "";
    public String nname = "unsaved.jaj";
    public String textfile = null;
    public OutputStream netsaveos;
    String cwd = "";
    String iwd = null;
    String bwd = null;
    String bgfile = "";

    TreeSet bookmarks = new TreeSet();
    static TreeSet allBookmarks = new TreeSet();
    TreeSet servermarks = new TreeSet();
    static TreeSet allServermarks = new TreeSet();
    static public String jarnalTmp = "jarnalTmp";

    static boolean pencentric = false;
    static boolean startMini = false;
    boolean mini = false;
    boolean micro = false;
    boolean barjarnal = false;
    boolean showOutline = false;

    Jtool jt = new Jtool();
    Jtool jtd = new Jtool();
    Jtool jth = new Jtool();
    Jtool jtbu = new Jtool();
    String middleButton = "Eraser";
    String rightButton = "Context Menu";
    String old_color = "black";
    float old_width = -1.0f;
    float fatWidth = 11.0f;
    boolean stickyRuler = false;
    boolean arrowhead = false;
    boolean temparrow = false;
    int markerweight = 10;
    boolean hideCursor = true;
    boolean textMode = false;
    boolean makeOverlay = false;
    boolean saveOnExit = false;
    boolean saveBookmarks = false;
    boolean updateBookmarks = true;
    boolean saveBg = false;
    boolean promptForNetSaveName = false;
    boolean oldPromptForNetSaveName;
    boolean urlencoded = false;
    boolean oldurlencoded;
    boolean saveSelfexecuting;
    boolean smoothStrokes = true;
    String email = "";
    String netServer = "";
    String netOptions = "";
    String uniqueID;
    String serverMessage = "<html><body>Nothing saved to server</body></html>";
    int viewQuality = 0x40;
    String highlighterStyle = "translucent";
    String lastAction = null;
    String userColor = null;
    int divwidth = 90;
    int outheight = 90;

    boolean alignToMargins = true;
    boolean bestFit = true;
    boolean absoluteScale = false;
    boolean showPageNumbers = true;
    boolean withBorders = false;

    boolean analyze = false;
    boolean trainrecog = false;
    boolean mscr = true;
    boolean ascr = false;

    boolean fullScreen = false;

    public Jarnal parentJarn = null;

    JarnalClient jcom;
    JarnalServer jserver;
    Jarnal jarn;
    int serverPort;
    static public int defaultServerPort = 1189;
    public String serverMsg = "";

    boolean dirty = false;	//file changed; used for save/exit dialogs
    boolean isNetSave = false;
    boolean fitWidth = true;    //should the width of the page be sized to the display area?
    int dragOp = 0;
    boolean thumbs = true;	//flag for multi-page views
    boolean threeup = true;	//if true multi-page view is three in a row, if false, multi-page view is 2x2
    boolean poverlay = false;	//if true pages are truncated by a factor of PO
    double PO = 0.25;
    boolean locked = false;	//is the display locked? see also dragOp = 100
    boolean replayActive = false;
    int activePage = 0;		//in a multipage view, which of the pages on the display is actually being used
				//jpages.getPage() is one-based, activePage is zero based
				//in the current implementation activePage should always be jpages.getPage()-1
    String actionMsg = "";	//status bar message reflecting choice of a drag-drop tool

    int sbarSize = 20;

    Number previewZoom = null;
    Number gotopage = null;

    Hashtable usersList = new Hashtable();

    public static String trans(String label){
		if(hlang == null) return label;
		String lbl = (String) hlang.get(label);
		if (lbl == null) return label;
		return lbl;
    }

    public static void initTrans(){
		hlang = null;

                System.out.println(language);
                InputStream in = Jarnal.class.getResourceAsStream("languages/" + language + ".txt");

		if (in == null) {
			return;
		}
		String s = null;
		try{
			s = new String(Jpages.streamToByteArray(in));
		}
		catch(Exception ex){System.err.println(ex); s = null;}
		if(s == null) {
			return;
		}
		hlang = new Hashtable();
		s = Jtool.replaceAll(s, "\r\n", "\n");
		s = Jtool.replaceAll(s, "\r", "\n");
		//s.replace("\r\n", "\n");
		//s.replace("\r", "\n");
		boolean done = false;
		int pos = 0;
		while(!done){
			pos = s.indexOf("\n");
			if(pos < 0) pos = s.length();
			String t = s.substring(0, pos);
			pos++;
			if(pos < s.length()) s = s.substring(pos);
			else done = true;
			pos = t.indexOf("===");
			if ((pos >= 0) && !(t.substring(0,1).equals("#"))) {
				String key = t.substring(0, pos).trim();
				t = t.substring(pos + 3);
				pos = t.indexOf("===");
				String value = t.substring(0, pos).trim();
				if((pos >= 0) && (!key.equals("")) && (!value.equals(""))) hlang.put(key, value);
			}
		}
    }

    public static int setarg(String[] args, int iarg, int len){
		String oldopenfile = openfile;
		openfile = args[iarg];
		int ret = iarg + 1;
		if(args[iarg].trim().equals("")) {
			openfile = oldopenfile;
			return ret;
		}
		if (openfile.equals("-t")){
			if(iarg >= len - 1) openfile = "";
			else {
				template = true;
				openfile = args[iarg + 1];
				ret++;
			}
			return ret;
		}
		if (openfile.equals("-b")){
			openfile = oldopenfile;
			if(iarg < len - 1) openbgfile = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-bb")){
			openfile = oldopenfile;
			if(iarg < len - 1) openbgfile = args[iarg + 1];
			bfilter bf = new bfilter(openbgfile);
			String bdir = (new File(openbgfile)).getAbsoluteFile().getParent();
			String fls[] = (new File(bdir)).list(bf);
			if(fls.length > 0){
				openfile = bdir + File.separator + fls[0];
				openbgfile = "";
				template = false;
			}
			ret++;
		}
		if(openfile.equals("-s")){
			openfile = oldopenfile;
			if(iarg < len - 1) savefile = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-connect")){
			openfile = oldopenfile;
			if(iarg < len - 1) startconnect = "server:/" + args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-text")){
			openfile = oldopenfile;
			if(iarg < len - 1) opentextfile = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-pdf")){
			openfile = oldopenfile;
			if(iarg < len - 1) pdffile = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-confdir")){
			openfile = oldopenfile;
			if(iarg < len - 1) confdir = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-m")){
			openfile = oldopenfile;
			if(iarg < len - 1) openmfile = args[iarg + 1];
			ret++;
		}
		if (openfile.equals("-lang")){
			openfile = oldopenfile;
			if(iarg < len - 1) language = args[iarg + 1];
			ret++;
			initTrans();
		}
		if(openfile.equals("-javagui")){
			openfile = oldopenfile;
			javagui = true;
		}
		if(openfile.startsWith("-mini")){
			openfile = oldopenfile;
			startMini = true;
		}
		if(openfile.startsWith("-fs")){
			openfile = oldopenfile;
			startfs = true;
		}
		if(openfile.startsWith("-p")){
			if(!openfile.equals("-p")){
				String s = openfile.substring(2);
				int n = s.indexOf("x");
				if(n >=0){
					int y = Integer.parseInt(s.substring(n + 1));
					int x = Integer.parseInt(s.substring(0, n));
					frameLocation = new Point(x, y);
					locationSet = true;
				}
			}					
			openfile = oldopenfile;
			setLocation = true;
		}
		if(openfile.equals("-n")){
			openfile = oldopenfile;
			showMenu = false;
		}
		if(openfile.equals("-g")){
			openfile = oldopenfile;
			Jbgs.useGS = true;
		}
		if(openfile.equals("-sg")){
			openfile = oldopenfile;
			Jbgs.silentGS = true;
		}
		if(openfile.equals("-pen")){
			openfile = oldopenfile;
			pencentric = true;
		}
		if(openfile.equals("-l")){
			if(iarg < len - 1) loadFiles.add(args[iarg + 1]);
			ret ++;
			openfile = oldopenfile;
		}
		return ret;
    }

   public static jrnlTimerListener getJrnlTimerListener(){
	Jarnal jarn = null;
	for (Iterator i = wins.iterator(); i.hasNext(); ) {
		jarn = (Jarnal)i.next();
		break;
	}
	return jarn.jtm;
   }
	

   public static void pipe(){
	if(HtmlPost.checkURL(openfile) && !isApplet) {
		HtmlPost hp = new HtmlPost(openfile, null, null, null, null, false);
		openfile = hp.pipe(".jaj");
	}
	if(HtmlPost.checkURL(openbgfile) && !isApplet) {
		HtmlPost hp = new HtmlPost(openbgfile, null, null, null, null, false);
		openbgfile = hp.pipe(".jbg");
	}
	if(HtmlPost.checkURL(openmfile)){
		HtmlPost hp = new HtmlPost(openmfile, null, null, null, null, false);
		meta = new String(hp.pipeBytes());
		openmfile = "";
	}
	try{
		if(openfile == null) openfile = "";
		if(openbgfile == null) openbgfile = "";
		if(openmfile == null) openmfile = "";
		if(!isApplet){
			if(!openfile.equals("")) openfile = (new File(openfile)).getCanonicalPath();
			if(!openbgfile.equals("")) openbgfile = (new File(openbgfile)).getCanonicalPath();
			if(!openmfile.equals("")) openmfile = (new File(openmfile)).getCanonicalPath();
		}
	}
	catch(java.io.IOException ex){ex.printStackTrace();}
    }

    private static void getMeta(){
	if(!openmfile.equals("")){
		try{
			FileInputStream fin = new FileInputStream(openmfile);
			int nmax = 10000; 
			byte b[] = new byte[nmax + 1];
			int nread = 0;
			while((nread = fin.read(b, 0, nmax)) >= 0)meta = meta + new String(b, 0, nread); 
		}
		catch(IOException ex){ex.printStackTrace();}
	}
    }

    public static void main(String[] args) {
        initTrans();
	loadImages();
	loadShell(false);
	int len = java.lang.reflect.Array.getLength(args);
	int iarg = 0;
	//for(int ioi = 0; ioi < len; ioi++) System.out.println(args[ioi]);
	while(iarg < len){
		iarg = setarg(args, iarg, len);
	}
	if(!javagui){
		try {
            		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        	} catch (Exception e) { e.printStackTrace(); }
	}
	loadShell(true);
	pipe();
	getMeta();
        initJ();
    }

    private static String parseShell(String shell){
	String z;
	shell = shell.trim();
	shell = shell + "\n";
	//String s = shell;
	String s = "";
	z = Jtool.getLine(shell, "printaround");
	if(z != null){
		if(z.equals("true")){
			s = s + "printaround=true\n";
			printaround = true;
		}
	}
	z = Jtool.getLine(shell, "pdfrenderer");
	if(z != null) gs = z;
	s = s + "pdfrenderer=" + gs + "\n";
	z = Jtool.getLine(shell, "ps2pdf");
	if(z != null) ps2pdf = z;
	s = s + "ps2pdf=" + ps2pdf + "\n";
	z = Jtool.getLine(shell, "browser");
	if(z != null) firefox = z;
	s = s + "firefox=" + firefox + "\n";
	z = Jtool.getLine(shell, "pdfconverter");
	if(z != null) pdfconverter = z;
	s = s + "pdfconverter=" + pdfconverter + "\n";
	z = Jtool.getLine(shell, "printpdf");
	if(z != null) printpdf = z;
	s = s + "printpdf=" + printpdf + "\n";
	z = Jtool.getLine(shell, "pdftotext");
	if(z != null) Jpages.pdftotext = z;
	s = s + "pdftotext=" + Jpages.pdftotext + "\n";
	z = Jtool.getLine(shell, "tzadjust");
	if(z != null) tzadjust = Integer.parseInt(z);
	s = s + "tzadjust=" + tzadjust + "\n";
	s = s.substring(0, s.length() - 1);
	return s;
    }

    private static File getConfDir(){
	String userhome = System.getProperty("user.home");
	if(confdir.equals("")) return new File(userhome);
	return new File(userhome + File.separator + confdir);
    }

    private static void writeShell(){
	if(isApplet) return;
	String shell = parseShell("");
	//File userDir = new File(System.getProperty("user.home"));
	//File userShell = new File(userDir, "jarnalshell.conf");
	File userShell = new File(getConfDir(), "jarnalshell.conf");
	try{
		FileOutputStream fos = new FileOutputStream(userShell);
		if(fos != null) fos.write(shell.getBytes());
	}
	catch(Exception ex){System.err.println("cannot write jarnalshell.conf");}
    }	

    private static void loadShell(boolean writeShell){
	String shell = jarnalshell;
	if(Jtool.checkMSWindows()){
		shell = jarnalshellwin;
		shell = Jtool.replaceAll(shell, "basepathlib", "\"" + Jtool.getBasepath() + "lib/pdftotext\" ");	
	}
	String oldshell = "";
	parseShell(shell);
	FileInputStream fis = null;
	if(!isApplet){ 
		//File userDir = new File(System.getProperty("user.home"));
		//File userShell = new File(userDir, "jarnalshell.conf");
		File userShell = new File(getConfDir(), "jarnalshell.conf");
		try{
			fis = new FileInputStream(userShell);
		}
		catch(Exception ex){fis = null;}
		if(fis != null) {
			try{
				ByteArrayOutputStream baost = new ByteArrayOutputStream();
				int nmin = 1000000;
				int nborg = 40000;
				int nmax = nmin + ( 5 * nborg); 
				byte b[] = new byte[nmax];
				int nread = 0;
				int noff = 0;
				while((nread = fis.read(b, noff, nborg)) >= 0){
					noff = noff + nread;
					if(noff > nmax - (2 * nborg)){
						baost.write(b, 0, noff);
						noff = 0;
					}
				}
				baost.write(b, 0, noff);
				byte c[] = baost.toByteArray();
				oldshell = new String(c);
				shell = parseShell(oldshell);
			}
			catch(Exception ex){System.err.println(ex); return;}
		}
		if((!oldshell.equals(shell)) && writeShell){
			try{
				FileOutputStream fos = new FileOutputStream(userShell);
				if(fos != null) fos.write(shell.getBytes());
			}
			catch(Exception ex){System.err.println("cannot write jarnalshell.conf");}
		}
	}
    }

    private void addParm(String z){
	String parm = getParameter(z);
	if(parm != null) meta2 = meta2 + z + "=" + parm + "\n";
    }

    public void init(){
	loadImages();
	meta = "";
	openfile = "";
	openbgfile = "";
	openmfile = "";
	doneMeta = false;
	setLocation = false;
	showMenu = true;
	template = false;
	meta2 = "[Globals]\n";
	addParm("netServer");
	addParm("netSaveName");
	addParm("setLocation");
	addParm("showMenu");
	addParm("promptForNetSaveName");
	addParm("URLEncode");
	String parm = getParameter("embed");
	embed = false;
	if(parm != null) {
		if(parm.trim().equals("true")) embed = true;
	}
	if(!isApplet){
		parm = getParameter("showGUI");
		if(parm != null){
			if(parm.trim().equals("true")) {
				isApplet = false;
				showGUI = true;
			}
		}
	}
	if(!showGUI) isApplet = true;
	parm = getParameter("jarnalFile");
	if(parm != null) openfile = parm;
	parm = getParameter("backgroundFile");
	if(parm != null) openbgfile = parm;
	parm = getParameter("templateFile");
	if(parm != null) {
		openfile = parm;
		template = true;
	}
	parm = getParameter("metaFile");
	if(parm != null) {
		openmfile = parm;
		pipe();
	}
	parm = getParameter("lang");
	if(parm != null) {
		language = parm;
		initTrans();
	}
	initNames();
	parm = getParameter("loadFile0");
	int ip = 1;
	while(parm != null){
		loadFiles.add(parm);
		parm = getParameter("loadFile" + ip);
		ip++;
	}		
	getMeta();
	meta2 = meta2 + "\n[Net Options]\n";
	parm = getParameter("p0");
	ip = 1;
	while(parm != null){
		meta2 = meta2 + parm + "\n";
		parm = getParameter("p" + ip);
		ip++;	
	}
    	try {
        	javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            	public void run() {
                	if(!embed) newJarnal("Jarnal");
			else newEJarnal("Jarnal");
            	}
        	});
    	} catch (Exception ex) {
        	System.err.println("createGUI didn't successfully complete " + ex);
		ex.printStackTrace();
		java.lang.reflect.InvocationTargetException ee = (java.lang.reflect.InvocationTargetException) ex;
		System.err.println(ee.getCause());
		System.err.println(ee.getTargetException());
    	}
    }
   
    public static Jarnal initJ() { 
	String title = "Jarnal";
	if(!openfile.equals("") && !template) {
		File temp = new File(openfile);
		title = title + " - " + temp.getName();    
	}
        if(!startMini) return newJarnal(title);
	return miniJarnal("miniJarnal");
    }

    public synchronized void checkClose(){
	nWins--;
	wins.remove(this);
	if(!mini) jrnlPane.doDisconnect();
	jrnlPane.jpages.doExit();
	System.gc();
	//if((nWins == 0) && !isApplet) System.exit(0);
	if(nWins == 0) try{System.out.println("Exiting..."); System.exit(0);} catch(SecurityException se){System.out.println("System exit error\n" + se);}
    }
    
    public static Jarnal newJarnal(String title){
        JFrame jrnlFrame = new JFrame(title);
        final Jarnal controller = new Jarnal();
	controller.initNames();
	controller.jarn = controller;
	wins.add(controller);
	JrnlPane jp = controller.buildUI(jrnlFrame);
        jrnlFrame.setSize(frameSize);  
	if(setLocation) jrnlFrame.setLocation(frameLocation);
	if(!pdffile.equals("")){
		controller.jrnlPane.doAction("-pdf");
		System.exit(0);
		return controller;
	}
        jrnlFrame.setVisible(true);
	if(controller.startBarJarnal) controller.jrnlPane.doAction("Thumbnail Bar");
	if(controller.startOutline) controller.jrnlPane.doAction("Outline");
	if(startfs) controller.jrnlPane.doAction("Full Screen");
        jp.requestFocus();
	jp.repaint();
	nWins++;
	if(startconnect != null) {
		String action = startconnect;
		startconnect = null;
		controller.jrnlPane.doAction(action);
	}
        return controller;
    }

    public static Jarnal miniJarnal(String title){
	JFrame jrnlFrame = new JFrame(title);
	return miniJarnal(jrnlFrame, jrnlFrame.getContentPane(), jrnlFrame.getToolkit());
    }

    public static Jarnal miniJarnal(JFrame jrnlFrame, Container cp, Toolkit tk){
	return miniJarnal(true, jrnlFrame, cp, tk);
    }

    public static Jarnal miniJarnal(boolean rtmenu, JFrame jrnlFrame, Container cp, Toolkit tk){
	final Jarnal controller = new Jarnal();
	controller.initNames();
	controller.jarn = controller;
	controller.fitWidth = false;
	JrnlPane jp = controller.buildMiniUI(rtmenu, jrnlFrame, cp, tk);
	jp.jpages.setHeight(2.00f);
	if(jrnlFrame != null) jp.jpages.setWidth(9.0f);
	else jp.jpages.setWidth(6.0f);
	jp.jpages.setPaper("Plain");
	if(jrnlFrame != null) jrnlFrame.setSize(new Dimension(480, 258));
	jp.jpages.setScale(1.0f);
	jp.setup();
	jp.doAction("Recognize");
	controller.statusBar.setFont(new Font("Sans Serif", Font.PLAIN, 16));
	if(jrnlFrame != null) {
		jrnlFrame.setVisible(true);
		jp.requestFocus();
		jp.repaint();
		nWins++;
	}
	return controller;
    }

    public static Jarnal barJarnal(String ofile, Jarnal parent, Container cp, Toolkit tk){
	openfile = ofile;
	final Jarnal controller = new Jarnal();
	controller.initNames();
	controller.jarn = controller;
	controller.fitWidth = true;
	JrnlPane jp = controller.buildBarUI(parent, cp, tk);
	controller.thumbs = true;
	controller.threeup = true;
	controller.activePage = jp.jpages.getPage() - 1;
	jp.setup();
	return controller;
    }

    public static Jarnal microJarnal(Container cp, Toolkit tk){
	final Jarnal controller = new Jarnal();
	controller.initNames();
	controller.jarn = controller;
	controller.micro = true;
	controller.fitWidth = false;
	JrnlPane jp = controller.buildMiniUI(null, cp, tk);
	jp.jpages.setHeight(2.00f);
	jp.jpages.setWidth(2.8f);
	jp.jpages.setPaper("Plain");
	jp.jpages.setScale(1.0f);
	jp.setup();
	jp.doAction("Recognize");
	controller.statusBar.setFont(new Font("Sans Serif", Font.PLAIN, 16));
	return controller;
    }
    
    public Jarnal newEJarnal(String title){
        JFrame jrnlFrame = null;
	this.jarn = this;
	JrnlPane jp = buildUI(jrnlFrame);
        jp.requestFocus();
	jp.repaint();
	nWins++;
        return this;
    }

    public JrnlPane buildBarUI(Jarnal parent, Container cp, Toolkit tk){
	barjarnal = true;
	toolkit = tk;
	tb1 = "";
	tb2 = "";
	boolean oldShowMenu = showMenu;
	showMenu = false;
	buildMenu(null);
	jmb = null;
	parentJarn = parent;
	JrnlPane jp = buildContainer(cp);
	showMenu = oldShowMenu;
	sp.addComponentListener(new JrnlSizeListener());
	return jp;
    }

    public JrnlPane buildMiniUI(JFrame jf, Container cp, Toolkit tk){
	return buildMiniUI(true, jf, cp, tk);
    }

    public JrnlPane buildMiniUI(boolean rtmenu, JFrame jf, Container cp, Toolkit tk){
	mini = true;
	gJrnlFrame = jf;
	if(jf != null){
		jf.addWindowListener(new JrnlClosing());
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addComponentListener(new JrnlSizeListener());
	}
	toolkit = tk;
	tb1 = "Backspace\nClear\nSymbol\nUser\nNumber Lock\nCapitalize\n";
	tb2 = "Backspace\nClear Out\nReturn\nCalculate\nPaste Out\n";
	if(!rtmenu) tb2 = "";
	if(micro) {
		tb1 = "Save Dictionaries\nseparator\nClear\n";
		tb2 = "";
	}
	jmb = null;
        JrnlPane jp = buildContainer(cp);
	jp.jpages.setPrint();
	statusBar.setText("Text: ");
	return jp;
    }	
  
    public JrnlPane buildUI(JFrame jf){
	if(!doneMeta) setMeta();
	bgfile = openbgfile;
	openbgfile = "";
	if(!embed){
		jf.addWindowListener(new JrnlClosing());
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addComponentListener(new JrnlSizeListener());
	}
        buildMenu(jf);
        gJrnlFrame = jf;
	if(!embed) toolkit = jf.getToolkit();
	else toolkit = this.getToolkit();
	if(embed) return buildContainer(getContentPane());
        return buildContainer(jf.getContentPane());
    }

    public void setClock(){
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("h:mm a");
	Date now = new Date();
	long dd = now.getTime() + (tzadjust * 60 * 60 * 1000);
	now = new Date(dd);
	clockLabel.setText(df.format(now));
    }

    public void setFileName(String iname){
		internalName = iname;
    }

    public String getFileName(){
	if(!internalName.equals("")) return internalName;
	if(fname.equals("")) return null;
	return "file://" + cwd + File.separator + fname;
    }

    public String getFileLoc(){
	return "file://" + cwd + File.separator + fname;
    }

    public static String getAbsoluteName(String cwd, String target){
	if(target.startsWith("..")){
		target = target.substring(2);
		target = cwd + target;
	}
	if(target.startsWith("file://")) target = target.substring("file://".length());
System.out.println(target);
	try{
		File tfile = new File(target);
		target = tfile.getCanonicalPath();					
	}
	catch(Exception ex){System.err.println(ex);}
System.out.println(target);
	return target;
    }

    public static String[] parseURL(String url){
	String ans[] = new String[3];
	ans[0] = "";
	ans[1] = "";
	ans[2] = "";
	String test = "";
	int n = url.indexOf("#");
	if(n > -1){
		if(n > 0) test = url.substring(0, n);
		ans[2] = url.substring(n);
	}
	else{
		n = url.indexOf("?");
		if(n > -1) {
			if(n > 0) test = url.substring(0, n);
			ans[2] = url.substring(n);
		}
		else test = url;
	}
	if(test.length() > 0){
		n = test.indexOf("://");
		if(n == -1) ans[1] = test;
		else{
			ans[0] = test.substring(0, n + 3);
			ans[1] = test.substring(n + 3);
		}
	}
	return ans;
    }
	
    public String getConf(){
	return jrnlPane.getConf();
    }

    public Jpages jpages(){
	return jrnlPane.jpages;
    }

    public void setMeta(){
		String s = meta;
		String z;
		s = s + "\n";
		String y = Jtool.getEntry(s, "[Globals]");
		z = Jtool.getLine(y, "backgroundFile");
		if(z != null) {
			openbgfile = z;
		}
		z = Jtool.getLine(y, "jarnalFile");
		if(z != null) {
			openfile = z;
		}
		z = Jtool.getLine(y, "templateFile");
		if(z != null) {
			template = true;
			openfile = z;
		}
		z = Jtool.getLine(y, "setLocation");
		if(z != null) {
			if(z.trim().equals("true")) setLocation = true;
		}
		z = Jtool.getLine(y, "email");
		if(z != null) {
			email = y;
		}
		z = Jtool.getLine(y, "showMenu");
		if(z != null) {
			if(z.trim().equals("false")) showMenu = false;
		}
		int ip = 1;
		z = Jtool.getLine(y, "loadFile0");
		while(z != null){
			loadFiles.add(z);
			z = Jtool.getLine(y, "loadFile" + ip);
			ip++;
		}
		pipe();
    }
    
//UI Definition    
    
   //build menu items
    public JMenuItem bmi(String action){
        JMenuItem item;
        item = new JMenuItem(trans(action));
        item.addActionListener(new JrnlActionListener(action));
        return item;
    }

    //build checkboxes
    public JCheckBoxMenuItem bcm(String action){
        JCheckBoxMenuItem item;
        item = new JCheckBoxMenuItem(trans(action));
        item.addActionListener(new JrnlActionListener(action));
        return item;
    }

    //build buttons
    public JButton bjb(String action){
        JButton item;
        item = new JButton(trans(action));
        item.addActionListener(new JrnlActionListener(action, item));
        return item;
    }

    //build buttons with an icon
    public JButton bjb(String action, Icon icon){
        JButton item;
        item = new JButton(icon);
        item.addActionListener(new JrnlActionListener(action, item));
	item.setToolTipText(trans(action));
        return item;
    }

    JMenu bmk;
    JMenu dbmk;
    JMenu allbmk;
    JMenu srv;
    JMenu dsrv;
    JMenu allsrv;
    JCheckBoxMenuItem soe;
    JCheckBoxMenuItem sbmk; 
    JCheckBoxMenuItem abmk;
    JCheckBoxMenuItem sbg;
    JCheckBoxMenuItem sbg2;
    JCheckBoxMenuItem pbgs;
    JCheckBoxMenuItem shbg;
    JCheckBoxMenuItem rbh;
    JCheckBoxMenuItem pam;
    JCheckBoxMenuItem pbf;
    JCheckBoxMenuItem pas;
    JCheckBoxMenuItem psp;
    JCheckBoxMenuItem wbr;
    JCheckBoxMenuItem pfnsn;
    JCheckBoxMenuItem recbox;
    JCheckBoxMenuItem train1;
    JCheckBoxMenuItem train2;
    JCheckBoxMenuItem recog1;
    JCheckBoxMenuItem recog2;
    JCheckBoxMenuItem scr1;
    JCheckBoxMenuItem scr2 ;
    JCheckBoxMenuItem scr3;
    JCheckBoxMenuItem ascr1;
    JCheckBoxMenuItem ascr2;
    JCheckBoxMenuItem ascr3;
    JCheckBoxMenuItem pencen;
    JCheckBoxMenuItem genQuality2;
    JCheckBoxMenuItem genQuality1;
    JCheckBoxMenuItem genQuality0;
    JCheckBoxMenuItem backQuality0;
    JCheckBoxMenuItem backQuality1;
    JCheckBoxMenuItem backQuality2;
    JCheckBoxMenuItem backSilentGS; 
    JCheckBoxMenuItem backUseGS;
    JCheckBoxMenuItem jcbSaveSelfexecuting;
    JCheckBoxMenuItem srcb;
    JCheckBoxMenuItem arcb;
    JCheckBoxMenuItem smstrk;

    JMenu textm;
    JMenu penm;

    private void initNames(){

    	bmk = new JMenu(trans("Bookmarks"));
    	dbmk = new JMenu(trans("Delete Bookmarks"));
    	allbmk = new JMenu(trans("Bookmarks"));
    	srv = new JMenu(trans("Servers"));
    	dsrv = new JMenu(trans("Delete Servers"));
    	allsrv = new JMenu(trans("Servers"));
    	soe = new JCheckBoxMenuItem(trans("Save On Exit"));
    	sbmk = new JCheckBoxMenuItem(trans("Save User Info")); 
    	abmk = new JCheckBoxMenuItem(trans("Autoupdate Bookmarks"));
    	sbg = new JCheckBoxMenuItem(trans("Save Background With File"));
    	sbg2 = new JCheckBoxMenuItem(trans("Save Background With File"));
    	pbgs = new JCheckBoxMenuItem(trans("Portable Backgrounds"));
    	shbg = new JCheckBoxMenuItem(trans("Show Background"));
    	rbh = new JCheckBoxMenuItem(trans("Repeating"));
    	pam = new JCheckBoxMenuItem(trans("Align to Margins"));
    	pbf = new JCheckBoxMenuItem(trans("Fit to Page"));
    	pas = new JCheckBoxMenuItem(trans("Absolute Scale"));
    	psp = new JCheckBoxMenuItem(trans("Show Page Numbers"));
    	wbr = new JCheckBoxMenuItem(trans("Print Borders"));
    	pfnsn = new JCheckBoxMenuItem(trans("Prompt for Net Save Name"));
    	recbox = bcm("Record");
    	train1 = bcm("Train Recognition");
    	train2 = bcm("Train Recognition");
    	recog1 = bcm("Recognize");
    	recog2 = bcm("Recognize");
    	scr1 = bcm("Minimize for Screenshot");
    	scr2 = bcm("Minimize for Screenshot");
    	scr3 = bcm("Minimize for Screenshot");
    	ascr1 = bcm("Entire Screen");
    	ascr2 = bcm("Entire Screen");
    	ascr3 = bcm("Entire Screen");
    	pencen = bcm("Pencentric");
    	genQuality2=bcm("High Quality");
    	genQuality1 = bcm("Normal Quality");
    	genQuality0 = bcm("Low Quality");
    	backQuality0 =bcm("Default Quality");
    	backQuality1 =bcm("Good Quality");
    	backQuality2 =bcm("Highest Quality");
    	backSilentGS =bcm("Silent External Renderer"); 
    	backUseGS =bcm("Use External Renderer");
    	jcbSaveSelfexecuting = bcm("Save Self Executing");
    	srcb = bcm("Sticky Ruler");
    	arcb = bcm("Arrow");
    	smstrk = bcm("Smooth Strokes");

    	textm = new JMenu(trans("Text"));
    	penm = new JMenu(trans("Pen"));
    }

    private JMenu buildInsertMenu(JCheckBoxMenuItem scr, JCheckBoxMenuItem ascr){
	JMenu toolsm = new JMenu(trans("Insert"));
	toolsm.add(bmi("Insert Page Before"));
	toolsm.add(bmi("Insert Page After"));
	toolsm.addSeparator();
	toolsm.add(bmi("Insert Jarnal Before"));
	toolsm.add(bmi("Insert Jarnal After"));
	toolsm.addSeparator();
	toolsm.add(bmi("Insert Background Before"));
	toolsm.add(bmi("Insert Background After"));
	toolsm.addSeparator();
	toolsm.add(bmi("Insert Background Text"));
	toolsm.add(bmi("Insert Link"));
	toolsm.addSeparator();
	if(!isApplet) toolsm.add(bmi("Insert Screenshot"));
	toolsm.add(bmi("Insert Overlay"));
	toolsm.add(bmi("Insert Image"));
	return toolsm;
    }

    private JMenu buildToolsMenu(JCheckBoxMenuItem train, JCheckBoxMenuItem recog, JCheckBoxMenuItem scr, JCheckBoxMenuItem ascr, boolean contextM){
	JMenu toolsm = new JMenu(trans("Tools"));
	toolsm.add(bmi("Eraser"));
	toolsm.add(bmi("Razor"));
	toolsm.add(bmi("Top Razor"));
	if(!contextM){
		toolsm.add(srcb);
		toolsm.add(arcb);
	}
	toolsm.add(bmi("Ruler"));
	toolsm.add(bmi("Select Rectangle"));
	toolsm.add(bmi("Select"));
	if(!contextM){
                JMenuItem item;
                toolsm.addSeparator();
                JMenu rigm = new JMenu(trans("Right Button"));
                item = new JMenuItem(trans("No Action"));
        	item.addActionListener(new JrnlActionListener("No Action rightButton"));
		rigm.add(item);
                item = new JMenuItem(trans("Button Pen"));
        	item.addActionListener(new JrnlActionListener("Button Pen rightButton"));
		rigm.add(item);
                item = new JMenuItem(trans("Eraser"));
        	item.addActionListener(new JrnlActionListener("Eraser rightButton"));
		rigm.add(item);
                item = new JMenuItem(trans("Context Menu"));
        	item.addActionListener(new JrnlActionListener("Context Menu rightButton"));
		rigm.add(item);
                item = new JMenuItem(trans("Select Rectangle"));
        	item.addActionListener(new JrnlActionListener("Select Rectangle rightButton"));
		rigm.add(item);
		item = new JMenuItem(trans("Last Action"));
        	item.addActionListener(new JrnlActionListener("xxLast Action rightButton"));
		rigm.add(item);
                toolsm.add(rigm);

		JMenu midm = new JMenu(trans("Middle Button"));
                item = new JMenuItem(trans("No Action"));
        	item.addActionListener(new JrnlActionListener("No Action middleButton"));
		midm.add(item);
                item = new JMenuItem(trans("Button Pen"));
        	item.addActionListener(new JrnlActionListener("Button Pen middleButton"));
		midm.add(item);
		item = new JMenuItem(trans("Eraser"));
        	item.addActionListener(new JrnlActionListener("Eraser middleButton"));
		midm.add(item);
                item = new JMenuItem(trans("Context Menu"));
        	item.addActionListener(new JrnlActionListener("Context Menu middleButton"));
		midm.add(item);
                item = new JMenuItem(trans("Select Rectangle"));
        	item.addActionListener(new JrnlActionListener("Select Rectangle middleButton"));
		midm.add(item);
		item = new JMenuItem(trans("Last Action"));
        	item.addActionListener(new JrnlActionListener("xxLast Action middleButton"));
		midm.add(item);
		toolsm.add(midm);

		toolsm.addSeparator();
		toolsm.add(bmi("mini Jarnal"));
		toolsm.add(bmi("Internal mini Jarnal"));
		toolsm.add(recog);
		recog.setState(analyze);
		JMenu recrec = new JMenu(trans("Recognize"));
		recrec.add(bmi("Recognize Page"));
		recrec.add(bmi("Undo Recognition"));
		if(!isApplet) {
			recrec.add(bmi("Edit Dictionaries"));
		}
		toolsm.add(recrec);
	}
	toolsm.addSeparator();
	toolsm.add(bmi("Stamp Date"));
	return toolsm;
    }

    private JMenu buildTextColorMenu(){
	JMenu tcolor = new JMenu(trans("Text Color"));
	tcolor.add(bmic("Black Text", "black"));
	tcolor.add(bmic("Blue Text", "blue"));
	tcolor.add(bmic("Green Text", "green"));
	tcolor.add(bmic("Gray Text", "gray"));
	tcolor.add(bmic("Magenta Text", "magenta"));	
	tcolor.add(bmic("Orange Text", "orange"));
	tcolor.add(bmic("Pink Text", "pink"));
	tcolor.add(bmic("Red Text", "red"));
	tcolor.add(bmic("White Text", "white"));
	tcolor.add(bmic("Yellow Text", "yellow"));
	return tcolor;
    }

    private JMenu buildTextSizeMenu(){
	JMenu size = new JMenu(trans("Text Size"));
	size.add(bmi(" 6pt"));
	size.add(bmi(" 7pt"));
	size.add(bmi(" 8pt"));
	size.add(bmi(" 9pt"));
	size.add(bmi("10pt"));
	size.add(bmi("11pt"));
	size.add(bmi("12pt"));
	size.add(bmi("13pt"));
	size.add(bmi("14pt"));
	size.add(bmi("15pt"));
	size.add(bmi("16pt"));
	size.add(bmi("18pt"));
	size.add(bmi("20pt"));
	size.add(bmi("22pt"));
	size.add(bmi("24pt"));
	size.add(bmi("26pt"));
	size.add(bmi("28pt"));
	size.add(bmi("32pt"));
	size.add(bmi("36pt"));
	size.add(bmi("40pt"));
	size.add(bmi("48pt"));
	size.add(bmi("54pt"));
	size.add(bmi("60pt"));
	size.add(bmi("66pt"));
	size.add(bmi("72pt"));
	size.add(bmi("80pt"));
	size.add(bmi("88pt"));
	size.add(bmi("96pt"));
	return size;
    }

    private JMenu buildFontMenu(){
	JMenu font = new JMenu(trans("Font"));
	String fn[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	for(int i = 0; i < fn.length; i++) font.add(bmi("Font " + fn[i]));
	return font;
    }

   public JMenuItem bmicc(String action){
	return bmic(action, action);
   }


    public JMenuItem bmic(String action, String color){
        JMenuItem item;
        item = new JMenuItem(trans(action), new colorIcon(color));
        item.addActionListener(new JrnlActionListener(action));
        return item;
    }

    private JMenu buildPenColorMenu(){
	JMenu color = new JMenu(trans("Color"));
	color.add(bmicc("black"));
	color.add(bmicc("blue"));
	color.add(bmicc("green"));
	color.add(bmicc("gray"));
	color.add(bmicc("magenta"));	
	color.add(bmicc("orange"));
	color.add(bmicc("pink"));
	color.add(bmicc("red"));
	color.add(bmicc("white"));
	color.add(bmicc("yellow"));
	return color;
    }

    private JMenu buildHighlighterStyleMenu(){
	JMenu bhs = new JMenu(trans("Highlighter Style"));
	bhs.add(bmi("Bottom Highlighter"));
	bhs.add(bmi("Translucent Highlighter"));
	bhs.add(bmi("Transparent Highlighter"));
	return bhs;
    }

    private JMenu buildPenWeightMenu(){
	JMenu pen = new JMenu(trans("Weight"));
	pen.add(bmi("Set Fine"));
	pen.add(bmi("Set Medium"));
	pen.add(bmi("Set Heavy"));
	pen.add(bmi("Set Fat"));
	return pen;
    }

    private JMenu buildTypeMenu(){
	JMenu type = new JMenu(trans("Type"));
	type.add(bmi("Space"));
	type.add(bmi("Return"));
	type.add(bmi("Backspace"));
	type.add(bmi("Delete"));
	return type;
    }

    JMenuBar jmb = new JMenuBar();
    JMenu helpMenu;
    JMenuItem pentextb;
    JMenuItem penundorecogb;
    JMenuItem textundorecogb;

    public void setContextMenu(){
	if(mini) return;
	if(barjarnal) return;
	if(analyze) {
		penundorecogb.setVisible(true);
		textundorecogb.setVisible(true);
	}
	else {
		penundorecogb.setVisible(false);
		textundorecogb.setVisible(false);
	}
	if(fullScreen) pentextb.setVisible(false);
	else pentextb.setVisible(true);
   }
	
	
    public void buildMenu(JFrame jf){
	if(jf != null) jf.setIconImage(jarnalIcon.getImage());		
        JMenu file = new JMenu(trans("File"));
        file.add(bmi("New"));
	if(!isApplet) file.add(bmi("Open"));
	file.add(bmi("OpenURL"));
	if(!isApplet){
		JMenu export = new JMenu(trans("Export"));
		export.add(bmi("Save Text As"));
		export.add(bmi("Save Html As"));
		export.add(bmi("Snapshot"));
		export.add(bmi("Export to PDF"));
		export.add(bmi("Export to TIFF"));
		file.add(bmi("New On Background"));
		file.addSeparator();
		file.add(bmi("Open Background"));
		file.add(bmi("Open Text"));
        	file.addSeparator();
        	file.add(bmi("Save"));
        	//file.add(bmi("Save As"));
		file.add(bmi("Save With Options"));
		file.add(export);
	}
	file.add(bmi("Network Save"));

	//file.addSeparator();
	file.add(bmi("Show Server Message"));
	file.addSeparator();
	JMenu bigOpt = new JMenu(trans("Options"));
	file.add(bigOpt);

	sbg.addActionListener(new JrnlActionListener("Save Background With File"));
	bigOpt.add(bmi("Network Save Options"));
	if(isApplet) bigOpt.add(bmi("Save Options"));
	//file.addSeparator();

	JMenu otherOptions = new JMenu(trans("Bookmark Options"));

	soe.addActionListener(new JrnlActionListener("Save On Exit"));

	//file.add(sbg);
	pfnsn.addActionListener(new JrnlActionListener("Prompt for Net Save Name"));

	if(!isApplet){
		//file.add(soe);
		sbmk.addActionListener(new JrnlActionListener("Save Bookmarks"));
		abmk.addActionListener(new JrnlActionListener("Autoupdate Bookmarks"));
		otherOptions.add(abmk);
		otherOptions.add(dbmk);
	}
	//file.add(sbmk);

	pencen.setSelected(pencentric);
	file.add(pencen);
	bigOpt.add(otherOptions);

	JMenu toolbarsm = new JMenu(trans("Toolbars"));
	toolbarsm.add(bmi("Restore Default Toolbars"));
	if(!isApplet){
		toolbarsm.add(bmi("Load Top Toolbar"));
		toolbarsm.add(bmi("Load Bottom Toolbar"));
		toolbarsm.add(bmi("Load Presentation Toolbar"));
	}
	bigOpt.add(toolbarsm);
	bigOpt.add(bmi("Restore Default Configuration"));
	if(!isApplet){
		bigOpt.add(bmi("Edit Current Template"));
		bigOpt.add(bmi("Adjust Time Zone"));
		file.add(bmi("Manage Internal Files"));
	}
	file.addSeparator();

	JMenu printOptions = new JMenu(trans("Print Options"));

	pam.setState(alignToMargins);
	pbf.setState(bestFit);
	pas.setState(absoluteScale);
	psp.setState(showPageNumbers);
	wbr.setState(withBorders);
	pam.addActionListener(new JrnlActionListener("alignToMargins"));
	pbf.addActionListener(new JrnlActionListener("bestFit"));
	pas.addActionListener(new JrnlActionListener("absoluteScale"));
	psp.addActionListener(new JrnlActionListener("showPageNumbers"));
	wbr.addActionListener(new JrnlActionListener("withBorders"));
	printOptions.add(pam);
	printOptions.add(pbf);
	printOptions.add(pas);
	printOptions.add(psp);
	printOptions.add(wbr);

	file.add(bmi("Print With Options"));
	if(!isApplet){
		file.addSeparator();
		file.add(allbmk);
	}
        file.addSeparator();
        file.add(bmi("Exit"));
    	jmb.add(file);
        
        JMenu edit = new JMenu(trans("Edit"));
        edit.add(bmi("Undo"));
	edit.add(bmi("Undo All"));
        edit.add(bmi("Redo"));
	edit.add(bmi("Redo All"));
	edit.add(bmi("Undo/Redo List"));
        edit.addSeparator();
	edit.add(recbox);
	JMenuItem playback = bmi("Playback");
	edit.add(playback);
	edit.addSeparator();
	edit.add(bmi("Find"));
	edit.addSeparator();
	edit.add(bmi("Cut"));
	edit.add(bmi("Copy"));
	edit.addSeparator();
	edit.add(bmi("Paste"));
	edit.addSeparator();
	edit.add(bmi("Clear"));
	edit.add(bmi("Delete"));
	edit.addSeparator();
	edit.add(bmi("Select All Text"));
	edit.add(bmi("Copy All Text"));
	edit.add(bmi("Copy Paper"));
	//edit.addSeparator();
	//edit.add(bmi("New Page Before"));
	//edit.add(bmi("New Page"));

	jmb.add(edit);
        
        JMenu view = new JMenu(trans("View"));
	view.add(bmi("Thumbs"));
	view.add(bmi("Continuous"));
	view.add(bmi("Overlay Pages"));
	view.addSeparator();
        view.add(bmi("Fit Width"));
        view.addSeparator();
        view.add(bmi("Zoom In"));
        view.add(bmi("Zoom Out"));
	view.add(bmi("Zoom"));
	if(!isApplet){
		view.addSeparator();
		view.add(bmi("Full Screen"));
		JMenu fs = new JMenu(trans("Full Screen Options"));
		fs.add(bmi("Shift Right"));
		fs.add(bmi("Shift Down"));
		view.add(fs);
	}
	view.addSeparator();
	view.add(bmi("Trap Colors"));
	view.addSeparator();
	
	JMenu genQuality = new JMenu(trans("Quality"));
	genQuality.add(genQuality2);
	genQuality.add(genQuality1);
	genQuality.add(genQuality0);
	JMenu backQuality = new JMenu(trans("Background Quality"));
	backQuality.add(backQuality0);
	backQuality.add(backQuality1);
	backQuality.add(backQuality2);
	backQuality.add(backUseGS);
	backQuality.add(backSilentGS);
	if(Jbgs.silentGS) {
		backSilentGS.setState(true);
		Jbgs.useGS = true;
	}
	if(Jbgs.useGS) backUseGS.setState(true);
	view.add(genQuality);
	view.add(backQuality);
	view.addSeparator();
	view.add(bmi("Page Overlay"));
	if(!isApplet){
		view.add(bmi("Outline"));
		view.add(bmi("Thumbnail Bar"));
		view.add(bmi("Update Thumbnail Bar"));
		view.addSeparator();
	}
	view.add(bmi("First Page"));
	view.add(bmi("Previous Page"));
	view.add(bmi("Next Page"));
	view.add(bmi("Last Page"));
	view.add(bmi("Go To Page"));
	view.addSeparator();
	view.add(bmi("Next Frame"));
	view.add(bmi("Previous Frame"));
	view.addSeparator();
	view.add(bmi("Highlight Lines"));
	view.addSeparator();
	view.add(bmi("Redraw Page"));
        jmb.add(view);

	JMenu transparency = new JMenu(trans("Transparency"));
	transparency.add(bmi("opaque"));
	transparency.add(bmi("translucent"));
	transparency.add(bmi("transparent"));
	transparency.add(bmi("other transparency"));

	JMenu pcolor = new JMenu(trans("Paper Color"));
	pcolor.add(bmi("white paper"));
	pcolor.add(bmi("yellow paper"));
	pcolor.add(bmi("pink paper"));
	pcolor.add(bmi("orange paper"));
	pcolor.add(bmi("blue paper"));
	pcolor.add(bmi("green paper"));
	
	if(!isApplet){
		JMenu insert = buildInsertMenu(scr1, ascr1);
		jmb.add(insert);
	}

	JMenu mtextm = new JMenu(trans("Text"));

	JMenu paper = new JMenu(trans("Format"));
	//paper.add(mtextm);
	paper.add(bmi("Text Style"));
	paper.addSeparator();
	paper.add(bmi("Paper and Background"));
	paper.add(bmi("Graph Paper"));
	paper.add(bmi("Set Size"));

	JMenu lines = new JMenu(trans("Lines"));
	lines.add(bmi("Thick Lines"));
	lines.add(bmi("Medium Lines"));
	lines.add(bmi("Thin Lines"));
	lines.add(bmi("Other Lines"));
	if(!isApplet) paper.add(bmi("Background Screenshot"));
	pbgs.addActionListener(new JrnlActionListener("Portable Backgrounds"));
	sbg2.addActionListener(new JrnlActionListener("Save Background With File"));
	shbg.addActionListener(new JrnlActionListener("Show Background"));
	JMenu overlay = new JMenu(trans("Overlay"));
	overlay.add(bmi("Insert Overlay"));
	overlay.addSeparator();
	overlay.add(bmi("Refresh Overlay"));
	JMenu ocolor = new JMenu(trans("Overlay Color"));
	ocolor.add(bmi("white overlay"));
	ocolor.add(bmi("yellow overlay"));
	ocolor.add(bmi("pink overlay"));
	ocolor.add(bmi("orange overlay"));
	ocolor.add(bmi("blue overlay"));
	ocolor.add(bmi("green overlay"));
	overlay.add(ocolor);
	overlay.addSeparator();
	overlay.add(bmi("Overlay Outline Thickness"));
	JMenu scolor = new JMenu(trans("Overlay Outline Color"));
	scolor.add(bmi("black outline"));
	scolor.add(bmi("blue outline"));
	scolor.add(bmi("green outline"));
	scolor.add(bmi("gray outline"));
	scolor.add(bmi("magenta outline"));	
	scolor.add(bmi("orange outline"));
	scolor.add(bmi("pink outline"));
	scolor.add(bmi("red outline"));
	scolor.add(bmi("white outline"));
	scolor.add(bmi("yellow outline"));
	overlay.add(scolor);
	paper.addSeparator();
	paper.add(bmi("Modify Selection"));
	paper.add(bmi("Overlay Style"));
	//paper.add(overlay);

	textundorecogb = bmi("Undo Recognition");
	textm.add(textundorecogb);
	textm.add(bmi("Text"));
	textm.addSeparator();
	textm.add(bmi("Cut"));
	textm.add(bmi("Copy"));
	textm.addSeparator();
	textm.add(bmi("Paste"));
	textm.addSeparator();

	textm.add(bmi("Bold Text"));
	textm.add(bmi("Italic Text"));
	textm.add(bmi("Underline Text"));
	textm.addSeparator();

	mtextm.add(bmi("Bold Text"));
	mtextm.add(bmi("Italic Text"));
	mtextm.add(bmi("Underline Text"));
	mtextm.addSeparator();

	textm.add(buildTextColorMenu());
	mtextm.add(buildTextColorMenu());

	textm.add(buildTextSizeMenu());
	mtextm.add(buildTextSizeMenu());

	textm.add(buildFontMenu());
	mtextm.add(buildFontMenu());

	mtextm.addSeparator();
	mtextm.add(bmi("Set Text Default"));

	textm.add(bmi("Text Style"));

	JMenu mpenm = new JMenu(trans("Pen"));

	penm.add(bmi("Undo"));
	penundorecogb = bmi("Undo Recognition");
	penm.add(penundorecogb);

	pentextb = bmi("Text");
	penm.add(pentextb);
	penm.add(bmi("Properties"));
	penm.addSeparator();

	penm.add(buildPenWeightMenu());
	mpenm.add(buildPenWeightMenu());

	penm.add(buildPenColorMenu());
	mpenm.add(buildPenColorMenu());

	mpenm.add(bmi("Base Pen Thickness"));
	mpenm.add(bmi("Base Highlighter Transparency"));
	mpenm.add(bmi("Fat Width"));
	mpenm.add(buildHighlighterStyleMenu());

	penm.addSeparator();
	mpenm.addSeparator();

	penm.add(bmi("Highlighter"));
	penm.add(bmi("Pen"));
	penm.add(bmi("Default Pen"));
	penm.add(bmi("Default Highlighter"));

	mpenm.add(bmi("Highlighter"));
	mpenm.add(bmi("Pen"));
	mpenm.add(bmi("Default Pen"));
	mpenm.add(bmi("Default Highlighter"));

	penm.addSeparator();
	penm.add(buildToolsMenu(train1, recog1, scr1, ascr1, true));

	mpenm.addSeparator();
	mpenm.add(bmi("Set Default"));
	mpenm.add(bmi("Set Button Pen"));

	jmb.add(paper);

	JMenu toolsm = buildToolsMenu(train2, recog2, scr2, ascr2, false);
	//toolsm.add(bmi("Modify Selection"), 0);
	smstrk.setState(smoothStrokes);
	toolsm.add(smstrk, 0);
	toolsm.add(bmi("Choose Pen"), 0);
	jmb.add(toolsm);

	connectMenu = new JMenu(trans("Collaborate"));
	startServer = bmi("Start Server");
	connectServer = bmi("Connect to Server");
	disconnectServer = bmi("Disconnect");
	disconnectActiveClient = bmi("Disconnect Active Client");
	
	setConnectMenu(false);
       
        helpMenu = new JMenu(trans("Help"));
	if(!isApplet) helpMenu.add(bmi("Check for Updates"));
	if(!isApplet) helpMenu.add(bmi("Documentation"));
	helpMenu.add(bmi("Memory Errors"));
	helpMenu.add(bmi("Hot Keys"));
	helpMenu.add(bmi("About"));
        
        if(showMenu && !embed) jf.setJMenuBar(jmb);
    }

    public void setConnectMenu(boolean server){
	connectMenu.removeAll();
	if(!server){
		connectMenu.add(startServer);
		connectMenu.add(connectServer);
		connectMenu.add(disconnectServer);
		connectMenu.add(disconnectActiveClient);
		disconnectActiveClient.setVisible(false);
		disconnectServer.setVisible(false);
		connectMenu.addSeparator();
		connectMenu.add(dsrv);
		connectMenu.addSeparator();
		connectMenu.add(allsrv);
	}
	else{
		connectMenu.add(disconnectServer);
		connectMenu.add(disconnectActiveClient);
	}
    }

    JMenu connectMenu;
    JMenuItem startServer;
    JMenuItem connectServer;
    JMenuItem disconnectServer;
    JMenuItem disconnectActiveClient;
    JButton saveButton;	
    JButton undoButton;
    JButton redoButton;
    JButton prevPageButton;
    JButton firstPageButton;
    JButton lastPageButton;
    public JButton handButton = null;
    public JLabel pageLabel = new JLabel();
    public JLabel clockLabel = new JLabel();
    JButton memoryButton;

    static ImageIcon hand;
    static ImageIcon handstop;
    static ImageIcon handyellow;
    static ImageIcon handmixed;
    static ImageIcon handmixed2;
    static ImageIcon fsave;
    static ImageIcon newdoc;
    static ImageIcon undo;
    static ImageIcon redo;
    static ImageIcon minus;
    static ImageIcon plus;
    static ImageIcon fit;
    static ImageIcon eraser;
    static ImageIcon erasetop;
    static ImageIcon erasebot;
    static ImageIcon bigeraser;
    static ImageIcon clonedoc;
    static ImageIcon red;
    static ImageIcon blk;
    static ImageIcon blu;
    static ImageIcon mgn;
    static ImageIcon grn;
    static ImageIcon highyel;
    static ImageIcon highmag;
    static ImageIcon highdef;
    static ImageIcon white;
    static ImageIcon left;
    static ImageIcon leftleft;
    static ImageIcon right;
    static ImageIcon rightright;
    static ImageIcon select;
    static ImageIcon selectrect;
    static ImageIcon newpage;
    static ImageIcon def;
    static ImageIcon fin;
    static ImageIcon med;
    static ImageIcon hev;
    static ImageIcon fat;
    static ImageIcon razor;
    static ImageIcon ruler;
    static ImageIcon thumbsico;
    static ImageIcon opico;
    static ImageIcon text;
    static ImageIcon clock;
    static ImageIcon threePages;
    static ImageIcon editcut;
    static ImageIcon editcopy;
    static ImageIcon editpaste;
    static ImageIcon cap;
    static ImageIcon num;
    static ImageIcon sym;
    static ImageIcon calc;
    static ImageIcon LC;
    static ImageIcon Spc;
    static ImageIcon Bsp;
    static ImageIcon browse;
    static ImageIcon fullscreen;
    static ImageIcon returnico;
    static ImageIcon userico;
    static ImageIcon multi;
    static ImageIcon arrow;
    static ImageIcon jarnalIcon;

    static private void loadImages(){

	fsave = new ImageIcon(Jarnal.class.getResource("images/filesave.png"));
	newdoc = new ImageIcon(Jarnal.class.getResource("images/newdoc.png"));
	undo = new ImageIcon(Jarnal.class.getResource("images/undo.png"));
	redo = new ImageIcon(Jarnal.class.getResource("images/redo.png"));
	minus = new ImageIcon(Jarnal.class.getResource("images/viewmag-.png"));
	plus = new ImageIcon(Jarnal.class.getResource("images/viewmag+.png"));
	fit = new ImageIcon(Jarnal.class.getResource("images/viewmagfit.png"));
	eraser = new ImageIcon(Jarnal.class.getResource("images/eraser.png"));
	erasetop = new ImageIcon(Jarnal.class.getResource("images/erasetop.png"));
	erasebot = new ImageIcon(Jarnal.class.getResource("images/erasebot.png"));
	bigeraser = new ImageIcon(Jarnal.class.getResource("images/bigeraser.png"));
        clonedoc = new ImageIcon(Jarnal.class.getResource("images/clonedoc.png"));
	red = new ImageIcon(Jarnal.class.getResource("images/pencilred.png"));
	blk = new ImageIcon(Jarnal.class.getResource("images/pencilblk.png"));
	blu = new ImageIcon(Jarnal.class.getResource("images/pencilblu.png"));
	grn = new ImageIcon(Jarnal.class.getResource("images/pencilgrn.png"));
	mgn = new ImageIcon(Jarnal.class.getResource("images/pencilmgn.png"));
	highyel = new ImageIcon(Jarnal.class.getResource("images/highlighteryel.png"));
	highmag = new ImageIcon(Jarnal.class.getResource("images/highlightermag.png"));
	highdef = new ImageIcon(Jarnal.class.getResource("images/highlighterdef.png"));
	white = new ImageIcon(Jarnal.class.getResource("images/whiteout.png"));
	left = new ImageIcon(Jarnal.class.getResource("images/1leftarrow.png"));
	leftleft = new ImageIcon(Jarnal.class.getResource("images/2leftarrow.png"));
	right = new ImageIcon(Jarnal.class.getResource("images/1rightarrow.png"));
	rightright = new ImageIcon(Jarnal.class.getResource("images/2rightarrow.png"));
	select = new ImageIcon(Jarnal.class.getResource("images/select.png"));
	selectrect = new ImageIcon(Jarnal.class.getResource("images/selectrect.png"));
	newpage = new ImageIcon(Jarnal.class.getResource("images/new.png"));
	def = new ImageIcon(Jarnal.class.getResource("images/default.png"));
	fin = new ImageIcon(Jarnal.class.getResource("images/fine.png"));
	med = new ImageIcon(Jarnal.class.getResource("images/medium.png"));
	hev = new ImageIcon(Jarnal.class.getResource("images/heavy.png"));
	fat = new ImageIcon(Jarnal.class.getResource("images/fat.png"));
	razor = new ImageIcon(Jarnal.class.getResource("images/razor.png"));
	ruler = new ImageIcon(Jarnal.class.getResource("images/ruler.png"));
	thumbsico = new ImageIcon(Jarnal.class.getResource("images/thumbs.png"));
	opico = new ImageIcon(Jarnal.class.getResource("images/opico.png"));
	threePages = new ImageIcon(Jarnal.class.getResource("images/threepages.png"));
	text = new ImageIcon(Jarnal.class.getResource("images/text.png"));
	clock = new ImageIcon(Jarnal.class.getResource("images/clock.png"));
	hand = new ImageIcon(Jarnal.class.getResource("images/hand.png"));
	handstop = new ImageIcon(Jarnal.class.getResource("images/handstop.png"));
	handyellow = new ImageIcon(Jarnal.class.getResource("images/handyellow.png"));
	handmixed = new ImageIcon(Jarnal.class.getResource("images/handmixed.png"));
	handmixed2 = new ImageIcon(Jarnal.class.getResource("images/handmixed2.png"));
	editcut = new ImageIcon(Jarnal.class.getResource("images/editcut.png"));
	editcopy = new ImageIcon(Jarnal.class.getResource("images/editcopy.png"));
	editpaste = new ImageIcon(Jarnal.class.getResource("images/editpaste.png")); 
	cap = new ImageIcon(Jarnal.class.getResource("images/cap.png"));
	num = new ImageIcon(Jarnal.class.getResource("images/num.png"));
	sym = new ImageIcon(Jarnal.class.getResource("images/sym.png"));
	calc = new ImageIcon(Jarnal.class.getResource("images/calc.png"));
	LC = new ImageIcon(Jarnal.class.getResource("images/lc.png"));
	Spc = new ImageIcon(Jarnal.class.getResource("images/spc.png"));
	Bsp = new ImageIcon(Jarnal.class.getResource("images/bsp.png"));
	browse = new ImageIcon(Jarnal.class.getResource("images/browser.png"));
	fullscreen = new ImageIcon(Jarnal.class.getResource("images/fullscreen.png"));
	returnico = new ImageIcon(Jarnal.class.getResource("images/rtn.png"));
	userico = new ImageIcon(Jarnal.class.getResource("images/user.png"));
	multi = new ImageIcon(Jarnal.class.getResource("images/multi.png"));
	arrow = new ImageIcon(Jarnal.class.getResource("images/arrow.png"));
	jarnalIcon = new ImageIcon(Jarnal.class.getResource("images/jarnal.png"));
    }

    boolean RQ = false;
    boolean SB = false;
    JButton jbo;
 
    private boolean addTool(JToolBar jtb, String action){

	ImageIcon ico = null;
	if(action.equals("Browse")){
		if(isApplet) return false;
		ico = browse;
	}
	if(action.equals("separator")){
		jtb.addSeparator();
		return false;
	}
	if(action.equals("Request Control")){
		if(RQ) return false;
		jtb.add(handButton);
		handButton.setVisible(false);
		RQ = true;
		return true;
	}
	if(action.equals("Page Number")){
		jtb.add(pageLabel);
		return true;
	}
	if(action.equals("Clock")){
		jtb.add(clockLabel);
		setClock();
		return true;
	}
	if(action.equals("Network Save")){
		if(SB) return false;
		SB = true;
		saveButton = bjb("Network Save", fsave);
		jtb.add(saveButton);
		isNetSave = true;
		return true;
	}
	if(action.equals("Save")){
		if(SB) return false;
		SB = true;
		jtb.add(saveButton);
		return true;
	}
	if(action.equals("Undo")){
		undoButton = bjb("Undo", undo);
		jtb.add(undoButton);
		return true;
	}
	if(action.equals("Redo")){
		redoButton = bjb("Redo", redo);
		jtb.add(redoButton);
		return true;
	}
        if(action.equals("First Page")){
		firstPageButton = bjb("First Page", leftleft);
		jtb.add(firstPageButton);
		return true;
	}
        if(action.equals("Previous Page")){
		prevPageButton = bjb("Previous Page", left);
		jtb.add(prevPageButton);
		return true;
	}
        if(action.equals("Last Page")){
		lastPageButton = bjb("Last Page", rightright);
		jtb.add(lastPageButton);
		return true;
	}
        if(action.equals("Next Page"))ico = right;
	else if(action.equals("Next Frame"))ico = right;
	else if(action.equals("New"))ico = newdoc;
	else if(action.equals("New On Background") && !isApplet)ico = newdoc;
	else if(action.equals("Network Save and Exit"))ico = fsave;
	else if(action.equals("Save Text")) ico = fsave;
	else if(action.equals("Save and Exit"))ico = fsave;
	else if(action.equals("Save Dictionaries"))ico = fsave;
        else if(action.equals("Zoom Out"))ico = minus;
        else if(action.equals("Fit Width"))ico = fit;
        else if(action.equals("Zoom In"))ico = plus;
	else if(action.equals("Thumbs"))ico = thumbsico;
	else if(action.equals("Overlay Pages"))ico = opico;
	else if(action.equals("Continuous"))ico = threePages;
	else if(action.equals("New Page"))ico = newpage;
	else if(action.equals("Default Pen"))ico = def;    
        else if(action.equals("Fine"))ico = fin;
        else if(action.equals("Medium"))ico = med;
        else if(action.equals("Heavy"))ico = hev;
	else if(action.equals("Fat"))ico = fat;
	else if(action.equals("green")){
		ico = grn;
		action = "Green";
	}
	else if(action.equals("magenta")){
		ico = mgn;
		action = "Magenta";
	}
	else if(action.equals("black")){
		ico = blk;
		action = "Black";
	}
	else if(action.equals("blue")){
		ico = blu;
		action = "Blue";
	}
	else if(action.equals("red")){
		ico = red;
		action = "Red";
	}
	else if(action.equals("multi")){
		ico = multi;
		action = "Choose Instrument Color";
		jbo = bjb(action, ico);
		jtb.add(jbo);
		return true;
	}
        else if(action.equals("Default Highlighter"))ico = highdef;
        else if(action.equals("Yellow Highlighter"))ico = highyel;
        else if(action.equals("Magenta Highlighter"))ico = highmag;
	else if(action.equals("White Out"))ico = white;
	else if(action.equals("Draw Arrow"))ico = arrow;
	else if(action.equals("Razor"))ico = razor;
	else if(action.equals("Ruler"))ico = ruler;
	else if(action.equals("Select"))ico = select;
	else if(action.equals("Select Rectangle"))ico = selectrect;
        else if(action.equals("Cut"))ico = editcut;
        else if(action.equals("Copy"))ico = editcopy;
        else if(action.equals("Paste"))ico = editpaste;	
	else if(action.equals("Paste Out")) ico = editpaste;
        else if(action.equals("Eraser"))ico = eraser;
	else if(action.equals("Clear"))ico = bigeraser;
	else if(action.equals("Clear Out")) ico = bigeraser;
	else if(action.equals("Duplicate Page"))ico = clonedoc;
	else if(action.equals("Top Eraser"))ico = erasetop;
	else if(action.equals("Bottom Eraser"))ico = erasebot;
	else if(action.equals("Stamp Date"))ico = clock;
	else if(action.equals("Text"))ico = text;
	else if(action.equals("Capitalize"))ico = cap;
	else if(action.equals("Number Lock"))ico = num;
	else if(action.equals("Symbol"))ico = sym;
	else if(action.equals("Calculate"))ico = calc;
	else if(action.equals("Space"))ico = Spc;
	else if(action.equals("Backspace"))ico = Bsp;
	else if(action.equals("Clear")) ico = bigeraser;
	else if(action.equals("Full Screen")) ico = fullscreen;
	else if(action.equals("Return")) ico = returnico;
	else if(action.equals("User")) ico = userico;
	if(ico != null) {
		jtb.add(bjb(action, ico));
		return true;
	}
	
	return false;
     }
 
    //strings defining the default toolbars    
    public String dtb1 = "#New\n#New On Background\nSave\n#Save Text\n#Network Save\n#Save and Exit\n#Network Save and Exit\nseparator\nClear\nCut\nCopy\nPaste\nseparator\nUndo\nRedo\nseparator\nFirst Page\nPrevious Page\nNext Page\n#Next Frame\nLast Page\nseparator\nZoom Out\nFit Width\nZoom In\nseparator\nContinuous\nThumbs\nOverlay Pages\nseparator\nDuplicate Page\nNew Page\nseparator\nseparator\nRequest Control\n";
    public String dtb2 = "Default Pen\nseparator\nFine\nMedium\nHeavy\nFat\nseparator\nblack\nblue\nred\nseparator\nmulti\nseparator\nDefault Highlighter\nYellow Highlighter\nMagenta Highlighter\nWhite Out\nseparator\nRazor\n#Top Eraser\n#Bottom Eraser\n#Draw Arrow\nRuler\nSelect Rectangle\nSelect\nEraser\nseparator\nStamp Date\nseparator\nBrowse\nText\n";
    public String dtb3 = "Full Screen\nseparator\nClear\nUndo\nRedo\nseparator\nFirst Page\nPrevious Page\nNext Page\nLast Page\nseparator\nDuplicate Page\nNew Page\nseparator\nRequest Control\nseparator\nDefault Pen\nFine\nMedium\nHeavy\nmulti\nDefault Highlighter\nYellow Highlighter\nWhite Out\nseparator\nRuler\nSelect\nEraser\nseparator\nBrowse\nseparator\nPage Number\nseparator\nClock\n";

    public String tb1 = dtb1;
    public String tb2 = dtb2;
    public String tb3 = dtb3;

    private boolean parseTB(JToolBar jtb, String tb){
	if(tb.trim().equals("")) return false;
	boolean ans = false;
	tb = tb.replace('\r', '\n');
	int n = tb.indexOf("\n");
	while(n >= 0){
		if(addTool(jtb, tb.substring(0, n))) ans = true;
		tb = tb.substring(n + 1, tb.length());
		n = tb.indexOf("\n");
	}
	return ans;
    }

    JToolBar jtb1;
    JToolBar jtb2;
    JPanel jpt;	
    boolean startInternalMini = false;
    boolean startBarJarnal = false;
    boolean startOutline = false;
	
    public JrnlPane buildContainer(Container container){

	outline = new Jarnout(this);

	Image cursorD = toolkit.getImage(Jarnal.class.getResource("images/dotblk.png"));
	Image cursorB = toolkit.getImage(Jarnal.class.getResource("images/blank.png"));
	Image cursorX = toolkit.getImage(Jarnal.class.getResource("images/box.png"));
	Image cursorW = toolkit.getImage(Jarnal.class.getResource("images/whitecursor.png"));
	Image cursorH = toolkit.getImage(Jarnal.class.getResource("images/highcursor.png"));
	Image cursorHa = toolkit.getImage(Jarnal.class.getResource("images/handcursor.png"));
	Image cursorCl = toolkit.getImage(Jarnal.class.getResource("images/clockcursor.png"));

	JPanel x = new JPanel(new BorderLayout());
	jpt = x;
        container.add(x, BorderLayout.NORTH);
	
	jth.highlighter = false;
	jth.transparency = 100;
	jth.color = "green";
	jth.fatWidth = fatWidth;
	jth.setWidth("Fat");
	jtbu.color = "white";
	jtbu.fatWidth = fatWidth;
	jtbu.setWidth("Fat");
		
        jrnlPane = new JrnlPane();
	defaultConf = jrnlPane.getConf();
	jrnlPane.jpages.outline = outline;
        jrnlPane.setup();

	jrnlPane.dotC = toolkit.createCustomCursor(cursorD, new Point(1,1), "dot");
	jrnlPane.blankC = toolkit.createCustomCursor(cursorB, new Point(1,1), "blank");
	jrnlPane.boxC = toolkit.createCustomCursor(cursorX, new Point(8,8), "box");
	jrnlPane.whiteC = toolkit.createCustomCursor(cursorW, new Point(8,8), "white");
	jrnlPane.highC = toolkit.createCustomCursor(cursorH, new Point(8,8), "high");
	jrnlPane.handC = toolkit.createCustomCursor(cursorHa, new Point(1,1), "hand");
	jrnlPane.clockC = toolkit.createCustomCursor(cursorCl, new Point(1,1), "clock");

	jrnlPane.open();
	if(!doneMeta) jrnlPane.setMeta();
	doneMeta = true;

	handButton = bjb("Request Control", hand);
	handButton.setVisible(false);
	if(isApplet) {
		saveButton = bjb("Network Save", fsave);
		isNetSave = true;
	}
	else saveButton = bjb("Save", fsave);

	int orient = javax.swing.SwingConstants.HORIZONTAL;
	if(mini) orient = javax.swing.SwingConstants.VERTICAL;
        jtb1 = new JToolBar(orient);
	if(parseTB(jtb1, tb1)){
		if(!mini) x.add(jtb1, BorderLayout.NORTH);
		else container.add(jtb1, BorderLayout.WEST);
	}

	if(mini && !micro){
		container.add(bjb("Space"), BorderLayout.SOUTH);
	}
        
        jtb2 = new JToolBar(orient);
	if(parseTB(jtb2, tb2)){
		if(!mini) x.add(jtb2, BorderLayout.SOUTH);
		else container.add(jtb2, BorderLayout.EAST);
	}

	if(jmb != null){
		if(RQ && !isApplet) jmb.add(connectMenu);
		jmb.add(helpMenu);
	}

	jrnlPane.setCursor();
        sp = new JScrollPane(jrnlPane);
        JScrollBar sb = sp.getHorizontalScrollBar();
        JScrollBar sv = sp.getVerticalScrollBar();
        sv.setUnitIncrement(10);
        sb.setUnitIncrement(100);
        sp.setInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
        container.add(sp, BorderLayout.CENTER);
		
        statusBar = new JLabel(trans("Edit"));
        if(!mini) {
		container.add(statusBar, BorderLayout.SOUTH);
        	jrnlPane.addKeyListener(new Jarnalkey());
	}
	else if(!micro) container.add(statusBar, BorderLayout.NORTH);

        jrnlPane.addMouseListener(new JrnlMouseListener());
        jrnlPane.addMouseMotionListener(new JrnlMouseMotionListener());

	jrnlPane.setVQ();
	
	dirty = false;

	if(!opentextfile.equals("")){
		opentextfile = "";
		jrnlPane.opentext();
	}

	if(startInternalMini) {
		jrnlPane.doAction("Internal mini Jarnal");
	}
		
        return jrnlPane;
    }

    public static int BOOKMARKS = 1;
    public static int SERVERS = 2;

    public void addToLists(String addition, int itype){
	boolean addToList = true;
	if(addition.equals("none")) addToList = false;
	boolean saveList = saveBookmarks;
	String prefix = "file:/";
	String delprefix = "delete:/";
	TreeSet list = bookmarks;
	TreeSet allList = allBookmarks;
	JMenu show = bmk;
	JMenu showAll = allbmk;
	JMenu delete = dbmk;
	
	switch(itype){
		case 1:
			if(!updateBookmarks) addToList = false;
			if(addition.indexOf(jarnalTmp) >= 0) addToList = false;	
			break;		
		case 2: 
			list = servermarks;
			allList = allServermarks;
			show = srv;
			showAll = allsrv;
			delete = dsrv;
			if(!updateBookmarks) addToList = false;
			prefix = "server:/";
			delprefix = "delete server:/";
			break;
	}

	if(addToList){			
		if(list.add(addition) && saveList) dirty = true;
	}
	show.removeAll();
	delete.removeAll();
	JMenuItem item = null;
	for (Iterator i = list.iterator(); i.hasNext(); ){
		String bname = (String) i.next();
		item = new JMenuItem(trans(bname));
		item.addActionListener(new JrnlActionListener(prefix + bname));
		show.add(item);
		item = new JMenuItem(trans(bname));
		item.addActionListener(new JrnlActionListener(delprefix + bname));
		delete.add(item);
	}
	showAll.removeAll();
	for (Iterator i = allList.iterator(); i.hasNext(); ){
		String bname = (String) i.next();
		item = new JMenuItem(trans(bname));
		item.addActionListener(new JrnlActionListener(prefix + bname));
		showAll.add(item);
	}		
    }


    public void addBookmarkAll(String bmark){
	allBookmarks.add(bmark);
	for (Iterator i = wins.iterator(); i.hasNext(); ) ((Jarnal)i.next()).addToLists(bmark, BOOKMARKS);
    }	

    public void addServerAll(String bmark){
	allServermarks.add(bmark);
	for (Iterator i = wins.iterator(); i.hasNext(); ){
		((Jarnal)i.next()).addToLists(bmark, SERVERS);
	}
    }

    String getBookmarks(){
	String s = "";
	for (Iterator i = bookmarks.iterator(); i.hasNext(); ){
		s = s + ((String)i.next()) + "\n";
	}
	return s;
    }

    String getServermarks(){
	String s = "";
	for (Iterator i = servermarks.iterator(); i.hasNext(); ){
		s = s + ((String)i.next()) + "\n";
	}
	return s;
    }

    void setMarks(String s, int itype){

	TreeSet allList = allBookmarks;

	switch(itype){
		case 2: allList = allServermarks; break;
	}

	if(s == null) s = "\nnone";
	s = s + "\n";
	int n = s.indexOf("\n");
	s = s.substring(n + 1);
	n = s.indexOf("\n");
	while(n > 0){
		String temp = s.substring(0,n);
		if(!temp.equals("none")) allList.add(temp);
		addToLists(temp, itype);
		s = s.substring(n + 1);
		n = s.indexOf("\n");
	}
	dirty = true;
    }

    private class JrnlClosing extends WindowAdapter {

	public void windowClosing(WindowEvent e){
		if(jrnlPane.winDone()) {
			Window win = e.getWindow();
			win.setVisible(false);
			win.dispose();
			checkClose();
		}
	}
	public void windowIconified(WindowEvent e){
		drawState = 0;
		if(!locked) dragOp = 0;
	}
    }

    private class JrnlSizeListener implements ComponentListener{
	public void componentResized(ComponentEvent e){
		if(barjarnal) {
			fitWidth = true;
			parentJarn.divwidth = jrnlPane.tpanel.getDividerLocation();
		}
		if(fitWidth) jrnlPane.resize();
		else jrnlPane.setup();
	}
	public void componentMoved(ComponentEvent e){
	}
	public void componentShown(ComponentEvent e){
	}
	public void componentHidden(ComponentEvent e){
	}
    }
	
    private class JrnlActionListener implements ActionListener {
        private String action;
        private JButton button;
	private boolean menuItem = false;
        public JrnlActionListener(String action, JButton button){
            this.action = action;
            this.button = button;
        }
        
        public JrnlActionListener(String action){
	    menuItem = true;
            this.action = action;
            button = null;
        }
        
        public void actionPerformed(ActionEvent e){
	    if(menuItem) menuflag = true;
	    jrnlPane.doAction(action);
	}
    }

//mouse drawing operations

    int drawState = 0;
    boolean menuflag = false;
    int cnt = 0;
    Point2D.Double x[] = new Point2D.Double[10000];
    int xx[] = new int[100];
    int yy[] = new int[100];
    int xxx[] = new int[100];
    int yyy[] = new int[100];
    int xmax, ymax, xmin, ymin, rad;
    int cc;
    int maxcc = 4;
    boolean dragOpFinished = false;
    boolean dragged = false;
    boolean lockClick = false;
    boolean pageChanged = false;
    boolean isPopup = false;
    int lastOp = 0;

    boolean isMiddleButton(MouseEvent e){
	if(e.getModifiers() != InputEvent.BUTTON2_MASK) return false;
	return true;
    }

    boolean isRightButton(MouseEvent e){
	if(e.isPopupTrigger()) return true;
	if(e.getModifiers() == InputEvent.BUTTON3_MASK) return true;
	return false;
    }	

    Jtool oldTool = new Jtool();

    private class JrnlScrollListener implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e){
			int n = e.getWheelRotation();
			if(n > 0) jrnlPane.doAction("Next Page");
			if(n < 0) jrnlPane.doAction("Previous Page");
		}
    }

    Point2D.Double popupPoint;
	
    private class JrnlMouseListener extends MouseAdapter {
	
        public void mousePressed(MouseEvent e) {
	    //System.out.println("mousePressed");
	    lockClick = false;
	    if(locked) {
		dragOp = 100;
		return;
	    }
            int X = e.getX();
	    int Y = e.getY();
	    lastOp = dragOp;
	    if(thumbs) pageChanged = jrnlPane.pickpage(X, Y);
	    if(dragOp == 76) {
		if(jrnlPane.hitLowerCorner(X, Y)) dragOp = 78;
		else if(jrnlPane.hitRectangle(X, Y)) dragOp = 77;
		else {
			drawState = 0;
			jrnlPane.invalidateP();
			jrnlPane.repaint();
			jrnlPane.setCursor();
			if(dragOp != 0) dragOpFinished = true;
			dragOp = 0;
			jrnlPane.endDragOp();
			jrnlPane.putdo(true);
			cnt = 0;;
		}
	    }
	    if(jrnlPane.choosepage(e.isControlDown(), e.isShiftDown())) return;
	    if(dragOp == 200) return;
	    isPopup = false;
	    jrnlPane.requestFocus();
	    //if(e.isPopupTrigger()){
	    if(isRightButton(e)){
                if(rightButton.equals("No Action")){
			isPopup = true;
			dragOp = 0;
			return;
		}
                if(rightButton.equals("Context Menu")){
                    jrnlPane.doAction(rightButton);
                    dragOp = 11;
                    setContextMenu();
                    if(textMode) textm.getPopupMenu().show(jrnlPane, X, Y);
                    else {
                            popupPoint = new Point2D.Double((double) X, (double) Y);
                            penm.getPopupMenu().show(jrnlPane, X, Y);
                    }
                    isPopup = true;
                    dragOp = 0;
                    return;
                }
                if(rightButton.equals("Eraser")){
                    jrnlPane.doAction(rightButton);
                }
                if(rightButton.equals("Select Rectangle")){
                    jrnlPane.doAction(rightButton);
                }
		if(rightButton.equals("Button Pen")){
			oldTool.fullCopy(jt);
			jt.fullCopy(jtbu);
		}
		else{
			jrnlPane.doAction(rightButton);
			if(jrnlPane.middleIgnore){
				isPopup = true;
				dragOp = 0;
				return;
			}
		}
	    }
	    if(isMiddleButton(e)){
		if(middleButton.equals("No Action")){
			isPopup = true;
			dragOp = 0;
			return;
		}
                if(middleButton.equals("Context Menu")){
                    jrnlPane.doAction(middleButton);
                    dragOp = 11;
                    setContextMenu();
                    if(textMode) textm.getPopupMenu().show(jrnlPane, X, Y);
                    else {
                            popupPoint = new Point2D.Double((double) X, (double) Y);
                            penm.getPopupMenu().show(jrnlPane, X, Y);
                    }
                    isPopup = true;
                    dragOp = 0;
                    return;
                }
                if(middleButton.equals("Eraser")){
                    jrnlPane.doAction(middleButton);
                }
                if(middleButton.equals("Select Rectangle")){
                    jrnlPane.doAction(middleButton);
                }
		if(middleButton.equals("Button Pen")){
			oldTool.fullCopy(jt);
			jt.fullCopy(jtbu);
		}
		else{
			jrnlPane.doAction(middleButton);
			if(jrnlPane.middleIgnore){
				isPopup = true;
				dragOp = 0;
				return;
			}
		}
	    }
	    Point2D.Double startL = new Point2D.Double((double) X, (double) Y);
 	    //this changes 113 and 114 so they shouldn't be received by
	    //clicked, released or dragged
	    //it should be OK to remove all references to 113 and 114 in those subroutines
	    if((dragOp == 114) || (dragOp == 113)){
		jrnlPane.addScrap(new Point2D.Double((double) e.getX(), (double) e.getY()));
		dragOp = 0;
		drawState = 0;
		jrnlPane.invalidateP();
		jrnlPane.repaint(1l);
		dragOp = 77;
	    }
	    jrnlPane.startStroke(startL);
	    cnt = 0;
	    dragged = false;
	    rad = (int) Math.ceil(jrnlPane.getStroke()/2);
	    jrnlPane.initDraw(X, Y);
	    dragOpFinished = false;
	    if(dragOp > 0) {
		jrnlPane.startDragOp();
		if((dragOp == 1) || (dragOp == 4) || (dragOp == 5) || (dragOp == 13)) {
			drawState = 0;
			jrnlPane.invalidateP();
			jrnlPane.repaint();
		}
	    }
	    if(textMode && (dragOp == 0)) jrnlPane.startText(startL);
        }

	// mouse clicks are sent after mouse releases
	// the eraser needs to know if it has been dragged so the ordinary eraser or clicked, so the precision eraser
	// this is done by tracking whether the mouse is dragged using the boolean dragged
	// the assumption is that after a mouse press there is always a drag or a click, but never both
	// if this is false (the java documentation doesn't say)
	// then it is necessary to stop using the click event and 
	// declare a click when the mouse is released and there has been no drag

	// on at least one system, Debian with Sun Java 1.5.0 it seems that clicks are being sent after drags
	// this is now locked out, so that after a mouse move, click is disabled

	// it would be a good idea to lock out the second button being processed
	// while the first one is being processed
	// strange clicking with both button can lead to strange results

	public void mouseClicked(MouseEvent e){
	    //System.out.println("mouseClicked");
	    if(lockClick) return;
	    if(isPopup) return;
	    if(dragOp == 76){
		dragOp = 0;
		drawState = 0;
		jrnlPane.repaint();
		return;
	    }
	    if(textMode && (dragOp != 113) && (dragOp != 3)) {
		jrnlPane.hitImage();
		return;
	    }
	    if(thumbs && pageChanged && (dragOp != 113) &&(dragOp != 114) && (dragOp != 117) && (dragOp != 200) && (dragOp != 3)){
		if (!locked) dragOp = 0;
		drawState = 0;
		return;
	    }
	    if((dragOp != 0) || (dragOpFinished)) {
		//the precision eraser
		if(dragOp == 3){
			jrnlPane.setDragOp(33);
			drawState = 0;
			jrnlPane.invalidateP();
			jrnlPane.dragOp(new Point2D.Double((double) e.getX(), (double) e.getY()));
			cnt = 0;
			jrnlPane.repaint();
			jrnlPane.setCursor();
			dragOpFinished = true;
			dragOp = 0;
			jrnlPane.endDragOp();
			jrnlPane.putdo(true);
		}
		dragOpFinished = true;
		if((dragOp == 113) || (dragOp == 114)){
			jrnlPane.addScrap(new Point2D.Double((double) e.getX(), (double) e.getY()));
			dragOp = 0;
			drawState = 0;
			jrnlPane.invalidateP();
			jrnlPane.repaint();
			jrnlPane.requestFocus();
		}
		if((dragOp != 200) && (dragOp != 117)) return;
	    }
	    Point2D.Double startL = new Point2D.Double((double) e.getX(), (double) e.getY());
	    if(dragOp == 200){
		jrnlPane.browse(startL);
		return;
	    }
	    if(dragOp == 117){
		jrnlPane.insertLink(startL);
		dragOp = 0;
		jrnlPane.clearActionMsg();
		jrnlPane.setStatus("");
		jrnlPane.requestFocus();
		return;
	    }
	    jrnlPane.click(startL);
	    drawState = 1; 
	    boolean isHigh = false;
	    if(jt.highlighter) isHigh = true;
	    if(jt.transparency != 255) isHigh = true;
	    if(!isHigh) {
		Rectangle r = jrnlPane.drawLast();
		if(thumbs || fullScreen) r = jrnlPane.offR(r, +1);
		jrnlPane.repaint(r);
		if(analyze){
			jrnlPane.analyzeClick();
		}
		return;
	    }
	    if(isHigh) drawState = 0;
	    if(drawState == 0) jrnlPane.invalidateP();
            jrnlPane.repaint();
            jrnlPane.requestFocus();
	}

	public void mouseReleased(MouseEvent e){
		//System.out.println("mouseReleased");
		if(isPopup) return;
		if(dragOp == 200) return;
		if(dragOp == 117) return;
		//draw, ruler or rectangular lasso
		if(dragOp <= 0){
			//rectangular lasso
			if(dragOp == -11){
				Point2D.Double pt = new Point2D.Double((double) e.getX(), (double) e.getY());
				jrnlPane.selRect(pt);
				cnt = 0;
				if(!makeOverlay){
					dragOp = 76;
					drawState = 0;
					return;
				}
				else{
					dragOp = 79;
					mouseReleased(e);
					return;
				}
			}
			if(textMode && (dragOp == 0)) {
				jrnlPane.putdo(true);
				return;
			}
			drawState = 1;
			for(int i = 0; i < cnt; i++) jrnlPane.stroke(x[i]);
			if((dragOp == -1) && arrowhead) {
				jrnlPane.setArrow();
				if(temparrow) {
					arrowhead = false;
					temparrow = false;
				}
			}
			if((dragOp == 0) && smoothStrokes){
				//cnt = 0;
				Rectangle r = jrnlPane.jpages.smooth();
				if(r != null){
					cnt = 0;
					jrnlPane.drawRect = r;
					jrnlPane.putdo(true);
					drawState = 101;
					if(thumbs || fullScreen) r = jrnlPane.offR(r, +1);
					jrnlPane.repaint(r);
					if(analyze) jrnlPane.analyze();
					return;
				}
			}	
			jrnlPane.putdo(true);
			boolean isHigh = false;
			if(jt.highlighter) isHigh = true;
			if(jt.transparency != 255) isHigh = true;
			if((dragOp == 0) && analyze && (cnt == 0) && (!isHigh)) jrnlPane.analyze();
			if(!isHigh && cnt > 0) {
				cnt = 0;
				Rectangle r = jrnlPane.drawLast();
				if(thumbs || fullScreen) r = jrnlPane.offR(r, +1);
				jrnlPane.clearActionMsg();
				jrnlPane.repaint(r);
				if((dragOp == 0) && analyze) jrnlPane.analyze();
				dragOpFinished = true;
				if(dragOp != 0) {
					if(!((dragOp == -1) && stickyRuler))dragOp = 0;
					jrnlPane.setStatus("");
				}
	    			if(isMiddleButton(e)){
					if(middleButton.equals("Button Pen"))jt.fullCopy(oldTool);
				}
				return;
			}
			if(!isHigh && (cnt == 0) && (dragOp == 0)) return;
			cnt = 0;
			if(dragOp == -1) jrnlPane.drawLast();
			if(dragOp == -1) drawState = 0;
			if(isHigh) drawState = 0;
			if(drawState == 0) jrnlPane.invalidateP();
			jrnlPane.clearActionMsg();
			jrnlPane.repaint();
			if(dragOp != 0) dragOpFinished = true;
			if(!((dragOp == -1) && stickyRuler))dragOp = 0;
			//note that dragOp and dragOpFinished 
			//will probably be set before the repaint actually occurs
			//the relevant part of the paint logic shouldn't use dragOp
	    		if(isMiddleButton(e)){
				if(middleButton.equals("Button Pen"))jt.fullCopy(oldTool);
			}
			return;
		}
		//read only
		if(dragOp == 100) return;
		//a text menu operation
		if(dragOp == 11) return;
		//eraser switches to precision eraser
		if((dragOp == 3) && !dragged) return;
		//inserting an image or pasting
		if((dragOp == 113) || (dragOp == 114)) {
			jrnlPane.setCursor();
			return;
		}
		drawState = 0;
		jrnlPane.invalidateP();
		jrnlPane.repaint();
		jrnlPane.setCursor();
		if(dragOp != 0) dragOpFinished = true;
		dragOp = 0;
		jrnlPane.endDragOp();
		jrnlPane.putdo(true);
		cnt = 0;
		if(lastOp == 76) jrnlPane.resetRectangle();
	}
    }
	
    private class JrnlMouseMotionListener extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
	    //System.out.println("mouseDragged");
	    lockClick = true;
	    if(isPopup) return;
            int X = e.getX();
	    int Y = e.getY();
	    //ruler, rectangular lasso
	    if((dragOp == -1) || (dragOp == -11)){
		cnt = 0;
		cc = 1;
	    }
	    x[cnt] = new Point2D.Double((double) X, (double) Y);
	    cnt++;
	    dragged = true;
	    xx[cc] = X;
            yy[cc] = Y;
            cc++;
	    if(X < xmin) xmin = X;
	    if(X > xmax) xmax = X;
	    if(Y < ymin) ymin = Y;
	    if(Y > ymax) ymax = Y;
	    //next step should only be possible if dragOp == 0
	    if((cc == maxcc) && !textMode){
	        int z[] = xxx;
		xxx = xx;
		xx = z;
		z = yyy;
		yyy = yy;
		yy = z;
		int xxmin = xmin;
		int xxmax = xmax;
		int yymin = ymin;
		int yymax = ymax;
		jrnlPane.initDraw(X, Y);
		drawState = 2; 
		jrnlPane.repaint(1l, xxmin - rad, yymin - rad, xxmax - xxmin + (2*rad), yymax - yymin + (2*rad));	
	    }
	    else if(textMode && (dragOp == 0)){
		cnt = 0;
		if(cc == maxcc){
			jrnlPane.dragText(X, Y);
			cc = 1;
		}
	    }
	    if (dragOp == 0) return;
	    //ruler or rectangular lasso
	    if((dragOp == -1) || (dragOp == -11)){
		drawState = -1; 
		if(dragOp == -11) drawState = -11;
		jrnlPane.clipR = new Rectangle(xmin-rad-jrnlPane.offX, ymin-rad-jrnlPane.offY, xmax - xmin + (2*rad), ymax - ymin + (2*rad));
		jrnlPane.repaint(1l, xmin - rad, ymin - rad, xmax - xmin + (2*rad), ymax - ymin + (2*rad));	
	    } 
	    //read only 
	    if((dragOp == 100) || (dragOp == 200)) {
		cc = 1;
		cnt = 0;
	    }
	    //inserting image
	    if((dragOp == 113) || (dragOp == 114) || (dragOp == 117)){
		cc = 1;
		cnt = 0;
	    }
	    //razor or select
	    else if(dragOp > 0){
		drawState = 0;
		jrnlPane.repaint();
		cc = 1;
	    }
        }
    }

    int keyCode;
    boolean ctrl = false;
    boolean alt = false;
    boolean shift = false;

    private class Jarnalkey extends KeyAdapter{
        int keyChar;
        int SHIFT = 16;
        int CTRL = 17;
        int ALT = 18;
	public void keyPressed(KeyEvent e){
		if(locked) dragOp = 100;
		if(dragOp == 100) return;
		keyCode = e.getKeyCode();
		//System.out.println("keyCode=" + keyCode);
		if(keyCode == SHIFT) shift = true;
		if(keyCode == CTRL) ctrl = true;
		if(keyCode == ALT) alt = true;
		String op = "adv";
		if(ctrl) op = "extend";
            	if(keyCode == 39) {				//VK_RIGHT
                	jrnlPane.textOp(op, 1, null);
                	jrnlPane.textOp("show", 0, null);
            	}
            	else if(keyCode == 37) {			//VK_LEFT
                	jrnlPane.textOp(op, -1, null);
                	jrnlPane.textOp("show", 0, null);
		}
		else if(keyCode == 33) {			//VK_PAGEUP
			jrnlPane.doAction("Previous Page");
		}
		else if(keyCode == 34) {			//VK_PAGEDOWN
			jrnlPane.doAction("Next Page");
		}
		else if(keyCode == 40) {			//VK_DOWN
			jrnlPane.doAction("Scroll Down");
		}
		else if(keyCode == 38) {			//VK_UP
			jrnlPane.doAction("Scroll Up");
		}
	}
        public void keyTyped(KeyEvent e) {
		keyChar = e.getKeyChar();
		if(ctrl){
			int test = keyChar - 96;
			if(test > 0) keyChar = test;
	    	}
		//don't lock out the escape key or you get stuck in fullscreen mode when the GUI is locked
		if(keyChar == 27) {
			jrnlPane.doAction("Escape");
			return;
		}
		if(dragOp == 100) return;
		//System.out.println("keyChar=" + keyChar);
            	if(keyCode == 8) {				//VK_BACKSPACE
                	jrnlPane.textOp("extendEmpty", -1, null);
                	jrnlPane.textOp("type", 0, "");
            	}
            	else if(keyCode == 127) {			//VK_DELETE
                	//jrnlPane.textOp("extendEmpty", 1, null);
       			//jrnlPane.textOp("type", 0, "");
			jrnlPane.doAction("Delete");
            	}
		else if(ctrl && (keyCode == 10)) jrnlPane.doAction("Next Frame");
		else if(keyCode == 10) jrnlPane.textOp("type", 0, "\n");
	    	else if(ctrl && (keyChar == 26)) jrnlPane.doAction("Undo");
	    	else if(ctrl && (keyChar == 24)) jrnlPane.doAction("Cut");
	    	else if(ctrl && (keyChar == 3)) jrnlPane.doAction("Copy");
		else if(ctrl && (keyChar == 1) && (lastAction != null)){
			jrnlPane.doAction(lastAction);
			jrnlPane.setStatus("");
		}
	    	else if(ctrl && (keyChar == 22)) jrnlPane.doAction("Paste");
		else if(ctrl && (keyCode == 66)) jrnlPane.textOp("style", 0, "Bold");
		else if(ctrl && (keyCode == 73)) jrnlPane.textOp("style", 0, "Italic");
		else if(ctrl && (keyChar == 25)) jrnlPane.doAction("Redo");//ctrl-y
		else if(ctrl && (keyChar == 21)) jrnlPane.doAction("Underline Text");//ctrl-u
		else if(ctrl && (keyChar == 14)) jrnlPane.doAction("New");//ctrl-n
		else if(ctrl && (keyChar == 15)) jrnlPane.doAction("Open");//ctrl-o
		else if(ctrl && (keyChar == 16)) jrnlPane.doAction("Print");//ctrl-p
		else if(ctrl && (keyChar == 17)) jrnlPane.doAction("Exit");//ctrl-q
		else if(ctrl && (keyChar == 19)) jrnlPane.doAction("Save");//ctrl-s

		else if (keyCode == java.awt.event.KeyEvent.VK_R && ctrl) jrnlPane.doAction("Ruler");
		else if (keyCode == java.awt.event.KeyEvent.VK_D && ctrl) jrnlPane.doAction("Arrow");
		else if (keyCode == java.awt.event.KeyEvent.VK_H && ctrl) jrnlPane.doAction("Default Highlighter");
		else if (keyCode == java.awt.event.KeyEvent.VK_W && ctrl) jrnlPane.doAction("Default Pen");
		else if (keyCode == java.awt.event.KeyEvent.VK_T && ctrl) jrnlPane.doAction("Text");
		else if (keyCode == java.awt.event.KeyEvent.VK_E && ctrl) jrnlPane.doAction("Eraser");
		else if (keyCode == java.awt.event.KeyEvent.VK_F && ctrl) jrnlPane.doAction("Select"); 

		else if(ctrl && ((keyChar == 61) || (keyChar == 43))) jrnlPane.doAction("Zoom In");//ctrl-+
		else if(ctrl && (keyChar == 45)) jrnlPane.doAction("Zoom Out");//ctrl--
		else if(!ctrl && !alt) jrnlPane.textOp("type", keyChar, null);
	}
        public void keyReleased(KeyEvent e){
		if(dragOp == 100) return;
		keyCode = e.getKeyCode();
		if(keyCode == SHIFT) shift = false;
		if(keyCode == CTRL) ctrl = false;
		if(keyCode == ALT) alt = false;
	}
    }

    static class bfilter  implements FilenameFilter{
	private String bgname = null;
	public bfilter(String bgpath){
		bgname = (new File(bgpath)).getName();
	}
	public boolean accept(File dir, String name){
		boolean ans = false;
		if(name.startsWith(bgname) && name.endsWith(".jaj")) ans = true;
		return ans;
	}
    }

    class ffilter extends javax.swing.filechooser.FileFilter{
	public boolean accept(File f){
		if(!f.isFile()) return true;
		String fn = f.getName();
		if(fn.length() < 4) return false;
		String fnext = fn.substring(fn.length() - 4, fn.length());
		if(fnext.equals(ext)) return true;
		return false;
	}
	public String getDescription(){
		return "Jarnal Files";
	}
    }

    private class JrnlDialogButtonListener implements ActionListener {
	private JTextField server;
	private JTextField saveName;
	private JTextArea options;
	private JDialog jw;
	private String action;

	public JrnlDialogButtonListener(String action, JTextField server, JTextField saveName, JTextArea options, JDialog jw){
		this.server = server;
		this.saveName = saveName;
		this.options = options;
		this.jw = jw;
		this.action = action;
	}
	public void actionPerformed(ActionEvent e){
		if(action.startsWith("paste")){
			try{
				String data = null;
				Transferable contents;	
				if(!isApplet){
					Clipboard clip = toolkit.getSystemClipboard();
					contents=clip.getContents(gJrnlFrame);
				}
				else contents = internalClipboard;
				if(contents.isDataFlavorSupported(DataFlavor.stringFlavor)) data = (String) contents.getTransferData(DataFlavor.stringFlavor);
				if(data != null) {
					String test = action.substring(5);
					if(test.equals("0")) server.replaceSelection(data);
					if(test.equals("1")) saveName.replaceSelection(data);
					if(test.equals("2")) options.replaceSelection(data);
				}
			}
			catch(Exception ex){ ex.printStackTrace();} 
		}
		if(action.equals("enter")){
			options.replaceSelection("\n");
		}
		if(action.equals("cancel")) {
			jw.setVisible(false);
			urlencoded = oldurlencoded;
			promptForNetSaveName = oldPromptForNetSaveName;
	        }
		if(action.equals("ok")){
			netServer = server.getText();
			netOptions = options.getText();
			nname = saveName.getText();
			jw.setVisible(false);	
			dirty = true;
			jrnlPane.setStatus("");
		}
	}
    }

    private class JrnlDialogBoxListener implements ItemListener {
	private String action;
	
	public JrnlDialogBoxListener(String action){
		this.action = action;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED)	{
			if(action.equals("urlencoded")) urlencoded = false;
			if(action.equals("promptForNetSaveName")) promptForNetSaveName = false;
		}
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if(action.equals("urlencoded")) urlencoded = true;
			if(action.equals("promptForNetSaveName")) promptForNetSaveName = true;
		}
	}
    }

    private class TrapItemListener implements ItemListener {
	private String action;

	public TrapItemListener(String action){
		this.action = action;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED)	{
			jrnlPane.trapAction(action, false);
		}
		if (e.getStateChange() == ItemEvent.SELECTED) {
			jrnlPane.trapAction(action, true);
		}
	}
    }
    private class TrapActionListener implements ActionListener {
	private String action;

	public TrapActionListener(String action){
		this.action = action;
	}

	public void actionPerformed(ActionEvent e) {
		if(action.startsWith("x")){
			String xcolor = action.substring(1);
			if(!xcolor.equals("none")){
				jrnlPane.doAction("Transparent Highlighter");
				jrnlPane.doAction("Yellow Highlighter");
				jrnlPane.doAction(xcolor);
				jrnlPane.doAction("Set Default");
				jrnlPane.doAction("Default Pen");
				jrnlPane.doAction(xcolor);
				jrnlPane.doAction("Set Default");
				userColor = xcolor;
			}
		}
		else jrnlPane.trapAction(action, true);
	}
   }

    private class manageActionListener implements ActionListener {
	private String action;

	public manageActionListener(String action){
		this.action = action;
	}

	public void actionPerformed(ActionEvent e) {
		jrnlPane.manageAction(action, e, null);
	}
   }


    private class manageListSelectionListener implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent e) {
		jrnlPane.manageAction("list", null, e);
	}
   }

   public jrnlTimerListener jtm = new jrnlTimerListener();
   public jrnlTimerListener ctm = new jrnlTimerListener("clock");
   public javax.swing.Timer clockTimer = new javax.swing.Timer(60000, ctm);
   private javax.swing.Timer jrnlTimer = new javax.swing.Timer(100, jtm);
   public Jarnbox jbcancel;

   class jrnlTimerListener extends Thread implements ActionListener{

	private Cursor oldcursor = null;
	private int tcnt = 0;
	private JDialog jw = null;
	private boolean setMemoryError = false;
	private boolean clockOn = false;
	private boolean isClock = false;
	public String action = null;
	private String message = "";
	private String msgTitle = "";

	public jrnlTimerListener(){}
	public jrnlTimerListener(String action){
		if(action.equals("clock")) isClock = true;
	}		

	public void actionPerformed(ActionEvent e) {
		if(isClock){
			setClock();
			return;
		}
		tcnt++;
		if(tcnt < 2) return;
		tcnt = 0;
		jrnlTimer.stop();
		if(setMemoryError){
			setMemoryError = false;
			JOptionPane.showConfirmDialog(gJrnlFrame, message, msgTitle, JOptionPane.DEFAULT_OPTION);
			drawState = 0;
			jrnlPane.repaint();
		}
		if(clockOn){
			clockOn = false;
			if(oldcursor != null) jrnlPane.setCursor(oldcursor);
			oldcursor = null;
			if(!embed) gJrnlFrame.setCursor(jrnlPane.standardC);
			if(jw != null) jw.setCursor(jrnlPane.standardC);
			jw = null;
		}
  	}

	public void setMessage(String message, String msgTitle){
		setMemoryError = true;
		this.message = message;
		this.msgTitle = msgTitle;
		jrnlTimer.start();
	}

	public void setMemoryError(){
		setMessage("Insufficient Memory to Display\nScale Has Been Reduced\nPlease Save Your Work\nSee Help for Increasing Memory" + memoryerrorstring, "Memory Error");
		memoryerrorstring = "";
	}

	public void setClockCursor(JDialog jw){
		if(jw != null){
			jw.setCursor(jrnlPane.clockC);
			this.jw = jw;
		}
		Cursor temp = jrnlPane.getCursor();
		if(temp != jrnlPane.clockC) oldcursor = temp;
		if(!embed) gJrnlFrame.setCursor(jrnlPane.clockC);
		jrnlPane.setCursor(jrnlPane.clockC);
		clockOn = true;
		jrnlTimer.start();
	}

	public void doActionWithCancel(String action, String title){
		jbcancel = new Jarnbox(gJrnlFrame, title, jarn, false);
		jbcancel.showCancel();
		jrnlTimerListener jtl = new jrnlTimerListener();
		jtl.action = action;
		jtl.start();
	}

	public void run(){
		if(action != null){
			int oldDragOp = dragOp;
			//locks the GUI so you don't mess up the document during the action
			dragOp = 100;
			jrnlPane.doAction(action);
			action = null;
			jbcancel.jw.setVisible(false);
			dragOp = oldDragOp;
		}
	}	
   } 
    
//Main window    
	
    class JrnlPane extends JPanel implements Printable {
    
	private Cursor standardC = new Cursor(Cursor.DEFAULT_CURSOR);
	public Cursor dotC = null; 
	public Cursor blankC = null;
	public Cursor boxC = null;
	public Cursor whiteC = null;
	public Cursor highC = null;
	public Cursor handC = null;
	public Cursor clockC = null;

	public void JrnlPane() {}

	public Jpages jpages = new Jpages();
	private BufferedImage gg;
	private Graphics2D gg2;

	private String clipdata;
	private TreeSet pageList = new TreeSet();

	public Rectangle clipR = new Rectangle(0,0,1,1);
	int bq;	

	public void standardCursor() {setCursor(standardC);}
	public void dotCursor() {setCursor(dotC);}
	public void blankCursor() {setCursor(blankC);}
	public void boxCursor(){setCursor(boxC);}
	public void whiteCursor(){setCursor(whiteC);}
	public void highCursor(){setCursor(highC);}
	public void textCursor(){setCursor(standardC);}
	public void handCursor(){setCursor(handC);}

	private boolean clearRegion = false;

	private boolean invalidateFlag = false;

//API begins here

	public void invalidateP(){
		jpages.setGraphics(null);
		invalidateFlag = true;
	}

	public void addScrap(Point2D.Double p){
		if(thumbs || fullScreen) p = offP(p, -1);
		if(dragOp == 114){
			jpages.pasteList(clipdata, p);		
		}
		else{
			if(nextScrap == null) return;
			jpages.addScrap(p, nextScrap);
		}
		dirty = true;
		setSave(false);
	}

	public void doanalyze(String str){
		if(str == null) return;
		if(str.equals("")) return;
		String test = str.substring(0,1);
		boolean started = false;
		while(test.equals("\n")){
			if(!started) textOp("extendEmpty", -1, null);
			else textOp("extend", -1, null);
			started = true;
			str = str.substring(1);
			test = str.substring(0,1);
		}
		menuflag = true;
		textOp("type", 0, str);
	}

	public void analyze(){
		jpages.setCurrent(jt);
		String str = jpages.analyze(jt, !mini);
		if(!mini) doanalyze(str);
		else minianalyze(str);
	}

	public void analyzeClick(){
		jpages.setCurrent(jt);
		String str = jpages.analyzeClick(jt, !mini);
		if(!mini) doanalyze(str);
		else minianalyze(str);
	}

	public void hitImage(){
		String test = jpages.hitImage();
		if(test == null) return;
		jpages.clickText();
		nextScrap = test;
		drawState = 0;
		invalidateP();
		jrnlPane.repaint();
	}

	public void advanceFrame(){
		int oldpage = jpages.getPage();
		Jtext test = jpages.checkFrame(1);
		if(jpages.getPage() != oldpage) {
			menuflag = true;
			setup();
		}
		if(test != null){
			nextFrame(test, true);	
		}
	}

	public void previousFrame(){
		int oldpage = jpages.getPage();
		Jtext test = jpages.checkFrame(-1);
		if(jpages.getPage() != oldpage) {
			menuflag = true;
			setup();
		}
		if(test != null){
			nextFrame(test, false);	
		}
	}

	Rectangle fixRR;	

	private Rectangle nextFrame(Jtext test, boolean moveforward){
		Rectangle oldr = jpages.getOldTextRectangle();
		if(oldr != null){
			int w = (int) jpages.getWidth();
			int h = (int) jpages.getHeight();
			int ww = (int) oldr.getWidth();
			int hh = (int) oldr.getHeight();
			int x = (int) oldr.getX();
			int y = (int) oldr.getY();
			Point2D.Double p = null;
			if(x + (2 * ww) < w){p = new Point2D.Double(x + ww, jpages.getY());}
			else if(y + (2 *hh) < h){p = new Point2D.Double(jpages.getX(), y + hh);}
			if(p != null) fixRR = jpages.getOldTextRectangle();
			dirty = true;
			jpages.setStartMark();
			int direc = 1;
			if(!moveforward) direc = -1;
			jpages.addFrame(jt, test, p, direc);		
			menuflag = true;
			setup();
		}
		return oldr;
	}

	public void textOp(String op, int data, String str){
		int oldpage = jpages.getPage();
		Jtext test = jpages.checkFrame(0);
		fixRR = null;
		if(jpages.getPage() != oldpage) {
			menuflag = true;
			setup();
		}
		if(test != null){
			Rectangle oldr = nextFrame(test, true);	
		}	
		if(op.equals("type")){
			if(str == null){
				char y = (char) data;
				Character z = new Character(y);
				str = z.toString();
			}
			Rectangle oldr = jpages.getOldTextRectangle();
			clipR = jpages.typeKey(str, jt);
			if((oldr != null) && (clipR != null)) clipR.add(oldr);
			dirty = true;
			op = "show";
		}
		if(op.equals("style")){
			clipR = null;
			Rectangle oldr = jpages.getOldTextRectangle();
			op = "show";
			dirty = true;
			if(str.equals("Bold")) clipR = jpages.setSelStyle(true, false, false, null, null, null);
			if(str.equals("Italic")) clipR = jpages.setSelStyle(false, true, false, null, null, null);
			if(str.equals("Underline")) clipR = jpages.setSelStyle(false, false, true, null, null, null);
			if(str.equals("Size")) clipR = jpages.setSelStyle(false, false, false, new Float(data), null, null);
			if(str.startsWith("Font ")){
				String font = str.substring(5);
				clipR = jpages.setSelStyle(false, false, false, null, font, null);
			}
			if(str.startsWith("Color")){
				String color = str.substring(5);
				clipR = jpages.setSelStyle(false, false, false, null, null, color);
			}
			if((oldr != null) && (clipR != null)) clipR.add(oldr);
		}
		if(op.equals("extendEmpty")){
			clipR = jpages.adv(data, 2);
		}
		if(op.equals("extend")){
			clipR = jpages.adv(data, 1);
		}
		if(op.equals("adv")){
			clipR = jpages.adv(data, 0);
		}
		if(op.equals("show")){
			drawState = 7;
			if(fixRR != null) {
				if(clipR == null) clipR = fixRR;
				else clipR.add(fixRR);
			}
			if(clipR != null) jrnlPane.repaint(1l, (int) (offX + clipR.getX()), (int) (offY + clipR.getY()), clipR.width, clipR.height);
		}
		if(test != null) jpages.setEndMark();
		setStatus("");
			
	}

	public void setArrow(){
		jpages.setArrow(markerweight);
	}

	public void stroke(Point2D.Double endL){
		if(thumbs || fullScreen) endL = offP(endL, -1);
		jpages.stroke(endL);
		boolean oldDirty = dirty;
		dirty = true;
		setSave(oldDirty);
	}

	Point2D.Double startL;
	public void startStroke(Point2D.Double endL){
		if(thumbs || fullScreen ) endL = offP(endL, -1);
		if(dragOp != -11) jpages.startStroke(endL, jt);
		else startL = endL;
	}

	public void selRect(Point2D.Double endL){
		Point2D.Double x[] = new Point2D.Double[2];
		x[0] = startL;
		x[1] = endL;
		if(thumbs || fullScreen){
		//	x[0] = offP(x[0], -1);
			x[1] = offP(x[1], -1);
		}
		if(!makeOverlay) jpages.startDragRect(x);
		else jpages.addOverlay(x, defaultOverlay);
	}

	public void startText(Point2D.Double endL){
		if(thumbs || fullScreen) endL = offP(endL, -1);
		clipR = jpages.startText(endL);
		setStatus("");
		if((clipR == null) || (gg == null)){
			drawState = 0;
			invalidateP();
			jrnlPane.repaint();
		}
		else{
			drawState = 7;
			jrnlPane.repaint(1l, (int) (offX + clipR.getX()), (int) (offY + clipR.getY()), clipR.width, clipR.height);
		}
	}

	public void dragText(int x, int y){
		if(thumbs || fullScreen){
			x = x - offX;
			y = y - offY;
		}
		clipR = jpages.dragText(x, y);
		drawState = 7;
		if(clipR != null) jrnlPane.repaint(1l, (int) (offX + clipR.getX()), (int) (offY + clipR.getY()), clipR.width, clipR.height);
		if(jpages.dragShape()){
			dirty = true;
			setStatus("");
		}
	}

	public void insertLink(Point2D.Double startL){
		if(thumbs || fullScreen) startL = offP(startL, -1);
		if(jpages.selectSingle(startL, jt)){
			String test = jpages.getlink();
			Jarnbox jb = new Jarnbox(gJrnlFrame, "Insert Link", jarn, true);
			jb.showLink(test);
			setStatus("");
		}
	}

	public void followLink(String test){
		if(test.equals("")) return;
		if(test.startsWith("pageref")){
			doAction(test);
			return;
		}
		if(test.startsWith(".."))test = getAbsoluteName(cwd, test);
		String prs[] = parseURL(test);
		String name = prs[1];
		String iname = "";
		if(prs[0].equals("")){
			iname = getFileName() + "?" + name;
			name = jpages.getExtraFile(name);
			test = "file://" + name + prs[2];
			prs[0] = "file://";
		}
		if(prs[0].equals("file://") && name.endsWith(".jaj")){
			int n = name.lastIndexOf(File.separator);
			String wd = "";
			String fn = name;
			if(n > -1) {
				wd = name.substring(0, n);
				fn = name.substring(n + 1);
			}
			Jarnal jn = null;
			for (Iterator i = wins.iterator(); i.hasNext(); ){
				Jarnal cn = (Jarnal)i.next();
				if(cn.getFileLoc().equals((prs[0] + name))){
					cn.gJrnlFrame.toFront();
					jn = cn;
					break;
				}
			}
			if(jn == null) {
				jn = openName(wd, fn);
				if(!iname.equals("")) jn.setFileName(iname);
			}
			String query = prs[2];
			if(query.length() > 0) query = query.substring(1);
			jn.jrnlPane.followLink(query);
		}
		else {		 
			String est = firefox;
			est = Jtool.replaceAll(est, "%1", Jtool.cmdQuote(test));
			try{
				Runtime.getRuntime().exec(est);	
			}
			catch(Exception ex){System.out.println("Cannot exec " + est);}
		}
	}

	public void browse(Point2D.Double startL){
		if(thumbs || fullScreen) startL = offP(startL, -1);	
		if(jpages.selectSingle(startL, jt)){
			String test = jpages.getlink();
			followLink(test);
		}
	}

	public void click(Point2D.Double endL){
		if(thumbs || fullScreen) endL = offP(endL, -1);
		jpages.click(endL, jt);
		boolean oldDirty = dirty;
		dirty = true;
		setSave(oldDirty);
	}

	public void endDragOp(){
		jpages.endDragOp();
		actionMsg = "";
	}

	public void clearActionMsg(){
		actionMsg = "";
	}

	public void setDragOp(int dragOp){jpages.setDragOp(dragOp);}
	public void startDragOp(){jpages.startDragOp(dragOp);}
	public void dragOp(Point2D.Double p){jpages.dragOp(p);}
	public void putdo(boolean pd){jpages.putdo(pd);}		
	public Rectangle drawLast(){return jpages.drawLast();}

//end of API

	public int offX = 0;
	public int offY = 0;
	public int foffX = 0;
	public int foffY = 0;

	public String defaultOverlay = Jtool.defaultOverlay;

	String prefix = "text: ";
	String input = "";
	LinkedList strokes = new LinkedList();
	String lastdic = null;

	public void minianalyze(String str){
		if(str == null) return;
		if(str.equals("")) return;
		if(micro && str.equals("<recognized character>")){
			jpages.getanalyze(jt).jdic.updateList1();		
			doAction("Clear");
			return;
		}
		String test = str.substring(0,1);
		int st = 0;
		while(test.equals("\n")){
			st = 1;
			input = input.substring(0, input.length() - 1);
			str = str.substring(1);
			test = str.substring(0,1);
		}
		if(prefix.equals("TEXT: ")) {
			str = str.toUpperCase();
		}
		miniadd(str.trim());
		Integer St = (Integer) strokes.removeLast();
		if(st == 1) St = (Integer) strokes.removeLast();
		St = new Integer(St.intValue() + 1);
		strokes.add(St);
	}


	public void miniadd(String str){
		if(str.equals("\n") && (input.length() == 0)) return;
		if(str.equals("\n")) input = input.substring(0, input.length() - 1);
		else {
			if(str.equals("<return>")) str = "\n";
			input = input + str;
			for(int i = 0; i < str.length(); i++) strokes.add(new Integer(0));
		}
		String inputDisplay = input.replaceAll("\n", "\\\\n"); 
		statusBar.setText(prefix + inputDisplay);
		StringSelection sel = new StringSelection(input);
		if(!isApplet){
			Clipboard clip = toolkit.getSystemClipboard();
			clip.setContents(sel, sel);
			clip = toolkit.getSystemSelection();
			if(clip != null) clip.setContents(sel, sel);
		}
		else internalClipboard = sel;
		if(parentJarn != null) {
			parentJarn.jrnlPane.doAction("Paste");
			parentJarn.jrnlPane.jpages.adv(-input.length(), 1);
                	parentJarn.jrnlPane.textOp("show", 0, null);
		}
	}

	public String miniAction(String action){
		//note that after miniAction is executed the ordinary doAction is executed
		//if miniAction has trapped the action is should reset it to "none" so nothing
		//happens when doAction is called
		if(action.equals("Save Dictionaries")){
			saveDics();
			action = "none";
		}
		if(action.equals("Calculate")) {
			String test = "" + (new Jarncalc(input)).calc();
			input = "";
			strokes = new LinkedList();
			miniadd(test);
			strokes = new LinkedList();
			action = "none";
		}
		if(action.equals("Space")) {
			miniadd(" ");
			action = "none";
		}
		if(action.equals("Return")) {
			miniadd("<return>");
			action = "none";
			if(parentJarn != null) action = "Clear Out";
		}
		if(action.equals("Number Lock")){
			Janalyze janal = jpages.getanalyze(jt);
			if(!prefix.equals("Num:  ")){
				prefix = "Num:  ";
				janal.setDictionary("num");
			}
			else  {
			 	prefix = "text: ";
				janal.setDictionary("base");
			}
			statusBar.setText(prefix + input);
			action = "none";
		}
		if(action.equals("Symbol")){
			Janalyze janal = jpages.getanalyze(jt);
			if(!prefix.equals("Symbol: ")){
				prefix = "Symbol: ";
				janal.setDictionary("sym");
			}
			else {
				prefix = "text: ";
				janal.setDictionary("base");
			}
			statusBar.setText(prefix + input);
			action = "none";
		}
		if(action.equals("User")){
			Janalyze janal = jpages.getanalyze(jt);
			if(!prefix.equals("User: ")){
				prefix = "User: ";
				janal.setDictionary("user");
			}
			else {
				prefix = "text: ";
				janal.setDictionary("base");
			}
			statusBar.setText(prefix + input);
			action = "none";
		}
			
		if(action.equals("Backspace")) {
			miniadd("\n");
			if(strokes.size() > 0){
				int st = ((Integer) strokes.removeLast()).intValue();
				for(int i = 0; i < st; i++) jpages.undo();
			}
			action = "none";
			if(strokes.size() == 0) action = "Clear";
		}
		if(action.equals("Capitalize")) {
			Janalyze janal = jpages.getanalyze(jt);
			if(!prefix.equals("TEXT: ")){
				prefix = "TEXT: ";
				janal.setDictionary("base");
			}
			else {
				prefix = "text: ";
				janal.setDictionary("base");
			}
			statusBar.setText(prefix + input);
			action = "none";
		}
		if(action.equals("Clear Out") || action.equals("Paste Out")){
			if(parentJarn != null) parentJarn.jrnlPane.doAction("Paste");
		}
			
		if(action.startsWith("Clear")) {
			JDictionaryEditor jd = jpages.getanalyze(jt).jdic;
			input = "";
			statusBar.setText(prefix);
			jpages.clearPage();
			jpages.setPrint();
			action = "none";
			Janalyze janal = jpages.getanalyze(jt);
			if(prefix.equals("Num:  ")){
				janal.setDictionary("num");
			}
			else prefix = "text: ";
			if(micro){
				janal.train = true;
				janal.sug0 = jd.janal.sug0;
				jd.janal = janal;
				janal.jdic = jd;
				janal.setDictionary(jd.dname);
			}
			statusBar.setText(prefix);
		}
		return action;
	}	


	public Rectangle offR(Rectangle r, int dir){
		if(!thumbs && !fullScreen) return r;
		r = new Rectangle((int)((dir*offX)+r.getX()), (int)((dir*offY)+r.getY()), (int)r.getWidth(), (int)r.getHeight());
		return r;
	}
	public Point2D.Double offP(Point2D.Double p, int dir){
		if(!thumbs && !fullScreen) return p;
		p = new Point2D.Double(((dir*offX)+p.getX()), ((dir*offY)+p.getY()));
		return p;
	}
	public int[] offXL(int x[], int cnt, int dir){
		if(!thumbs && !fullScreen) return x;
		for(int i = 0; i < cnt; i++) x[i] = (offX * dir) + x[i];
		return x;
	}
	public int[] offYL(int x[], int cnt, int dir){
		if(!thumbs && !fullScreen) return x;
		for(int i = 0; i < cnt; i++) x[i] = (offY * dir) + x[i];
		return x;
	}
	public Point2D.Double[] offPL(Point2D.Double x[], int cnt, int dir){
		if(!thumbs && !fullScreen) return x;
		for(int i = 0; i < cnt; i++) x[i] = offP(x[i], dir);
		return x;
	}

	public boolean choosepage(boolean pick, boolean extend){
		if(!pick && !extend && pageList.isEmpty()) return false;
		if(!pick && !extend) pageList = new TreeSet();
		else if(pick || pageList.isEmpty()){
			Integer iii = new Integer(1 - jpages.getPage());
			if(pageList.contains(iii)) pageList.remove(iii);
			else pageList.add(iii);
		}
		else{
			int ii = jpages.getPage() - 1;
			int top = -((Integer)pageList.first()).intValue();
			int bot = -((Integer)pageList.last()).intValue();
			if(ii < bot) bot = ii;
			if(ii > top) top = ii;
			pageList = new TreeSet();
			for(ii = top; ii >=bot; ii--){
				pageList.add(new Integer(-ii));
			}
		}
		dragOp = 0;
		isPopup = true;
		drawState = 0;
		repaint();
		return true;
	}

	public boolean pickpage(int x, int y){
		if(!thumbs) return false;
		int oldAP = jpages.getPage() - 1;
		int delta = 0;
		int w = getTWidth();
		int h = getTHeight();
		int r = y / h;
		if(r >= nr()) r = nr() - 1;
		int c = x / w;
		if(c >= nc()) c = nc() - 1;
		offX = c * w;
		offY = r * h;
		activePage = (r * nc()) + c;
		delta = activePage - oldAP;
		if(barjarnal){
			parentJarn.gotopage = new Integer(activePage + 1);
			parentJarn.jrnlPane.doAction("GoToPage");
		}
		if(delta != 0) {
			jpages.nextPage(delta);
			if(showOutline) outline.synchPage(delta);
			gg = jpages.getGraphics();
			if(gg != null){
				if(gg2 != null) gg2.dispose();
				gg2 = gg.createGraphics();
				setHints(gg2);
				jpages.setGraphics2D(gg2);
			}
			else drawState = 0;
			setStatus("");
			return true;
		}
		return false;		
	}

	private String fixDate(int i){
		String s = "" + i;
		if(s.length() == 1) s = "0" + s;
		return s;
	} 

	public void initDraw(int X, int Y){
		cc = 0;
	    	xx[cc] = X;
            	yy[cc] = Y;
		cc = 1;
		xmin = X;
		xmax = X;
		ymin = Y;
		ymax = Y;
	}

	public float getStroke(){
		float x = jt.getWidth();
		boolean isHigh = false;
		if(jt.highlighter) isHigh = true;
		//if(jt.transparency != 255) isHigh = true;
		if((jt.type.equals("Fat")) && isHigh) x = jt.getHeavy();
		return x * jpages.getScale();
	}

        private JCheckBox bct(String action){
        	JCheckBox item;
        	item = new JCheckBox(trans(action));
        	item.addItemListener(new TrapItemListener(action));
        	return item;
    	}

	private JButton bbt(String action){
		JButton item;
		item = new JButton(trans(action));
		item.addActionListener(new TrapActionListener(action));
		return item;
	}

	ButtonGroup bgr = new ButtonGroup();

	private JRadioButton brb(String action){
		String lbl = (String) usersList.get(action.substring(1));
		if(lbl == null) lbl = "";
		JRadioButton ans = new JRadioButton(lbl);
		ans.setActionCommand(action);
		bgr.add(ans);
		ans.addActionListener(new TrapActionListener(action));
		return ans;
	}

	JCheckBox trapEnable = bct("Enable Color Trap");
	JCheckBox trapBlack = bct("Black");
	JCheckBox trapBlue = bct("Blue");
	JCheckBox trapGreen = bct("Green");
	JCheckBox trapGray = bct("Gray");
	JCheckBox trapMagenta = bct("Magenta");
	JCheckBox trapOrange = bct("Orange");
	JCheckBox trapPink = bct("Pink");
	JCheckBox trapRed = bct("Red");
	JCheckBox trapWhite = bct("White");
	JCheckBox trapYellow = bct("Yellow");

	JDialog trapDialog;

	private void setTraps(boolean on){
		trapBlack.setSelected(on);
		trapBlue.setSelected(on);
		trapGreen.setSelected(on);
		trapGray.setSelected(on);
		trapMagenta.setSelected(on);
		trapOrange.setSelected(on);
		trapPink.setSelected(on);
		trapRed.setSelected(on);
		trapWhite.setSelected(on);
		trapYellow.setSelected(on);
	}

	private void setTraps(){
		trapBlack.setSelected(jpages.trapc[0]);
		trapBlue.setSelected(jpages.trapc[1]);
		trapGreen.setSelected(jpages.trapc[2]);
		trapGray.setSelected(jpages.trapc[3]);
		trapMagenta.setSelected(jpages.trapc[4]);
		trapOrange.setSelected(jpages.trapc[5]);
		trapPink.setSelected(jpages.trapc[6]);
		trapRed.setSelected(jpages.trapc[7]);
		trapWhite.setSelected(jpages.trapc[8]);
		trapYellow.setSelected(jpages.trapc[9]);
	}

	public void trapAction(String action, boolean selected){
		if(action.startsWith("Exit")){
			jpages.trapColors = false;
			trapDialog.setVisible(false);
		}
		if(action.equals("All On")) setTraps(true);
		if(action.equals("All Off")) setTraps(false);
		if(action.equals("Redraw")) doAction("Redraw Page");
		if(action.equals("Black")) jpages.trapc[0] = selected;
		if(action.equals("Blue")) jpages.trapc[1] = selected;
		if(action.equals("Green")) jpages.trapc[2] = selected;
		if(action.equals("Gray")) jpages.trapc[3] = selected;
		if(action.equals("Magenta")) jpages.trapc[4] = selected;
		if(action.equals("Orange")) jpages.trapc[5] = selected;
		if(action.equals("Pink")) jpages.trapc[6] = selected;
		if(action.equals("Red")) jpages.trapc[7] = selected;
		if(action.equals("White")) jpages.trapc[8] = selected;
		if(action.equals("Yellow")) jpages.trapc[9] = selected;
		if(action.equals("Enable Color Trap")) jpages.trapColors = selected;
		if(action.equals("Load Users")){
			String oldcwd = cwd;
			String temp = getFile("Open Users", false);
			String uwd = cwd;
			cwd = oldcwd;
			String userfile = uwd + File.separator + temp;
			String instr = null;
			try{
				instr = new String(jpages.streamToByteArray(new FileInputStream(userfile)));
			}
			catch(Exception ex){System.err.println(ex); instr = null;}
			if(instr != null) {
				usersList = Jtool.readConf(instr);
				trapDialog.setVisible(false);
				trapDialog();
				dirty = true;
			}
		}
		if(action.equals("Clear Users")){
			usersList = new Hashtable();
			trapDialog.setVisible(false);
			trapDialog();
			dirty = true;
		}
	}

	private JButton bmt(String action){
		JButton item;
		item = new JButton(trans(action));
		item.addActionListener(new manageActionListener(action));
		return item;
	}

	private void manageAction(String action, ActionEvent e, ListSelectionEvent el){
		if(action.equals("combo")){
        		JComboBox cb = (JComboBox)e.getSource();
        		String selstr = (String)cb.getSelectedItem();
			setManageList(selstr);
		}
		if(action.startsWith("Exit")){
			manageDialog.setVisible(false);
		}
		if(action.equals("list")){
    			if (el.getValueIsAdjusting() == false) {
				manageSel = (String) manageList.getSelectedValue();
				if(manageSel != null) manageSave.setEnabled(true);
				else manageSave.setEnabled(false);
			}
		}
		if(action.equals("Paste")){
			//only images and links to extras can be pasted
			if(manageSel != null){
				if(manageType.equals("Extras") && !isApplet){
					StringSelection sel = new StringSelection(manageSel);
					Clipboard clip = toolkit.getSystemClipboard();
					clip.setContents(sel, sel);
				}
				else{
					nextScrap = manageSel;
					dragOp = 113;
					standardCursor();
					setStatus("");
				}
			}
		}
		if(action.equals("Open")){
			if(manageType.equals("Toolbars")){
				if(manageSel.equals("Top Toolbar")) doAction("Load Top Toolbar");
				if(manageSel.equals("Bottom Toolbar")) doAction("Load Bottom Toolbar");
				if(manageSel.equals("Presentation Toolbar")) doAction("Load Presentation Toolbar");
				dirty = true;
				setStatus("");
				return;
			}
			if(manageType.equals("Active Images") || manageType.equals("Inactive Images")){
				doAction("Insert Image");
				setManageList(manageType);
				return;
			}
			if((manageType.equals("Active Backgrounds")) || (manageType.equals("Inactive Backgrounds"))){
				doAction("Open Background");
				setManageList(manageType);
				return;
			}
			if(manageType.equals("Users")){
				doAction("Load Users");
				return;
			}
			String fname = "";
			fname = manageOpen();
			if(fname != null){
				if(manageType.equals("Extras")) jpages.addExtra(fname, manageData);
				if(manageType.equals("Conf")) setConf(new String(manageData));
				dirty = true;
				setManageList(manageType);
				setStatus("");
			}
		}
		if(action.equals("Save")){
			if(manageSel != null){
				manageData = null;
				if(manageType.equals("Outline")) manageData = jpages.outline.getXML().getBytes();
				if(manageType.equals("Extras"))	manageData = jpages.getExtra(manageSel);
				if(manageType.equals("Toolbars") && manageSel.equals("Top Toolbar"))
					manageData = tb1.getBytes();
				if(manageType.equals("Toolbars") && manageSel.equals("Bottom Toolbar"))
					manageData = tb2.getBytes();
				if(manageType.equals("Toolbars") && manageSel.equals("Presentation Toolbar"))
					manageData = tb3.getBytes();
				if(manageType.equals("Conf"))
					manageData = getConf().getBytes();
				if(manageType.equals("Active Images")) manageData = jpages.getImage(manageSel);
				if(manageType.equals("Inactive Images")) manageData = jpages.getImage(manageSel);
				if(manageType.equals("Active Backgrounds")) manageData = jpages.getBackground(manageSel);
				if(manageType.equals("Inactive Backgrounds")) manageData = jpages.getBackground(manageSel);
				if(manageType.equals("Users")) manageData = Jtool.writeConf(usersList).getBytes();
				if(manageType.equals("Pages")){
					int n = Integer.parseInt(manageSel);
					manageData = jpages.savePage(n - 1).getBytes();
				}
				if(manageType.equals("Undo/Redo Stacks") && manageSel.startsWith("redostack"))
					manageData = jpages.saveRedo(getConf());
				if(manageType.equals("Undo/Redo Stacks") && manageSel.startsWith("undostack"))
					manageData = jpages.saveUndo(getConf());
				if(manageType.equals("Toolbars") && manageSel.equals("Bottom Toolbar"))
					manageData = tb2.getBytes();
				if(manageData != null) manageSave();
			}
		}
		if(action.equals("Delete")){
			if(manageSel != null){
				if(manageType.equals("Extras")) jpages.deleteExtra(manageSel);
				if(manageType.equals("Inactive Images")) jpages.deleteImage(manageSel);
				if(manageType.equals("Inactive Backgrounds")) jpages.deleteBackground(manageSel);
				if(manageType.equals("Users")) usersList = new Hashtable();
				dirty = true;
				setManageList(manageType);
			}
		}		
	}

	private void manageSave(){
		if(manageSel == null) return;
		JFileChooser fd = new JFileChooser(manageDir);
		String sug = manageSel;
		if(pencentric){
			String ans = (new Jarnbox(gJrnlFrame, "Save Name", jarn, true)).getString(sug);
			if(ans != null) sug = ans;
		}
		fd.setSelectedFile(new File(sug));
		int test = fd.showSaveDialog(gJrnlFrame);
		if(test == JFileChooser.CANCEL_OPTION) return;
		//String temp = fd.getName(fd.getSelectedFile());
		String temp = fd.getSelectedFile().getName();
		if (temp == null) return;
		File tdir = fd.getCurrentDirectory();
		manageDir = tdir.getPath();
		try{
			FileOutputStream out = new FileOutputStream(manageDir + File.separator + temp);
			out.write(manageData);
			out.close();
		}
		catch(Exception ex){System.err.println("file write error in manageSave");}
	}

	private String manageOpen(){
		String oldcwd = cwd;
		if(manageDir != null) cwd = manageDir;
		String temp = getFile("Open " + manageType, false);
		if(temp == null) return temp;
		manageDir = cwd;
		cwd = oldcwd;
		String userfile = manageDir + File.separator + temp;
		try{
			manageData = jpages.streamToByteArray(new FileInputStream(userfile));
		}
		catch(Exception ex){System.err.println(ex); manageData = null;}
		return temp;
	}

	private void setManageList(String selstr){
	    manageType = selstr;
	    String test[] = null;
	    manageDelete.setEnabled(true);
	    managePaste.setText("Paste");
	    managePaste.setEnabled(false);
	    manageOpen.setEnabled(true); 
	    manageSave.setEnabled(false);
	    if(selstr.equals("Toolbars")) {
			test = new String[3];
			test[0] = "Top Toolbar";
			test[1] = "Bottom Toolbar";
			test[2] = "Presentation Toolbar";
			manageDelete.setEnabled(false);
	    }
	    if(selstr.equals("Conf")) {
		manageDelete.setEnabled(false);
		test = new String[1]; 
		test[0] = "Conf";
	    }
	    if(selstr.equals("Outline")) {
		manageDelete.setEnabled(false);
		manageOpen.setEnabled(false);
		String outXML = jpages.outline.getXML();
		test = new String[1];
		if(outXML != null) test[0] = "outline.xml";
		else test[0] = "No Outline Available";
	    }
	    if(selstr.equals("Undo/Redo Stacks")){
		manageDelete.setEnabled(false);
		test = new String[2];
		String xname = fname;
		if(xname.equals("")) xname = "jaj";
		test[0] = "undostack." + xname;
		test[1] = "redostack." + xname;
		manageOpen.setEnabled(false);
	    }
	    if(selstr.equals("Users")){
		test = new String[1];
		test[0] = "Users";
	    }
	    if(selstr.equals("Active Backgrounds")){
		manageDelete.setEnabled(false);
		test = jpages.getBgs(true);
	    }
	    if(selstr.equals("Inactive Backgrounds")) {
		test = jpages.getBgs(false);
	    }
	    if(selstr.equals("Pages")){
		manageDelete.setEnabled(false);
		manageOpen.setEnabled(false);
		int npages = jpages.getPages();
		test = new String[npages];
		for(int i = 0; i < npages; i++)test[i] = "" + (i + 1);
	    }
	    if(selstr.equals("Active Images")){
		manageDelete.setEnabled(false);
		managePaste.setEnabled(true);
		test = jpages.getImages(true);
	    }
	    if(selstr.equals("Inactive Images")) {
		managePaste.setEnabled(true);
		test = jpages.getImages(false);
	    }
	    if(selstr.equals("Extras")) {
		test = jpages.getExtras();
	    	managePaste.setText("Copy Link");
	    	managePaste.setEnabled(true);
	    }
	    manageList.setListData(test);
	}

	byte[] manageData;
	String manageDir = null;
	String manageType;
	String manageSel;
	JList manageList;
	JDialog manageDialog;
	JButton manageDelete;
	JButton managePaste;
	JButton manageOpen;
	JButton manageSave;

	private void manageDialog(){
	    JDialog jw = new JDialog(gJrnlFrame, "Manage Internal Files");
	    manageDialog = jw;
            jw.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    jw.getContentPane().setLayout(new FlowLayout());
	    JPanel top = new JPanel();
	    String sels[] = {"Conf", "Outline", "Toolbars", "Pages", "Users", "Undo/Redo Stacks", "Active Backgrounds", "Inactive Backgrounds", "Active Images", "Inactive Images", "Extras"};
	    JComboBox jc = new JComboBox(sels);
	    jc.setSelectedIndex(9);
	    jc.addActionListener(new manageActionListener("combo"));
	    top.add(jc);
	    jw.getContentPane().add(top);
	    JPanel mid = new JPanel();
	    manageList = new JList(); 
	    manageList.addListSelectionListener(new manageListSelectionListener());
	    manageList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    manageList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    manageList.setVisibleRowCount(-1);
	    JScrollPane listScroller = new JScrollPane(manageList);
	    listScroller.setPreferredSize(new Dimension(500, 160));
	    mid.add(listScroller);
	    jw.getContentPane().add(mid);
	    JPanel bot = new JPanel();
	    JPanel bot2 = new JPanel();
	    manageDelete = bmt("Delete");
	    bot.add(manageDelete);
	    managePaste = bmt("Paste");
	    bot.add(managePaste);
	    jw.getContentPane().add(bot);
	    manageOpen = bmt("Open");
	    bot2.add(manageOpen);
	    manageSave = bmt("Save");
	    bot2.add(manageSave);
	    bot2.add(bmt("Exit This Dialog"));
	    jw.getContentPane().add(bot2);
            jw.setSize(new Dimension(530, 300));
	    setCenter(jw);
            jw.setVisible(true);
	    setManageList("Extras");
	    jw.addWindowListener(new dialogClosing(new manageActionListener("Exit This Dialog")));
	}
	    

	private void trapDialog(){
	    jpages.trapColors = true;
	    trapEnable.setSelected(jpages.trapColors);
	    setTraps();
	    JDialog jw = new JDialog(gJrnlFrame, "Color Trap");
	    trapDialog = jw;
            jw.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    jw.getContentPane().setLayout(new FlowLayout());
	    JPanel top = new JPanel();
	    top.setLayout(new GridLayout(0,2));
	    top.add(new JLabel(""));
	    top.add(new JLabel(trans("Set")));
	    top.add(trapEnable);
	    JRadioButton xnone = brb("xnone");
	    xnone.setSelected(true);
	    top.add(xnone);
	    top.add(trapBlack);
	    top.add(brb("xblack"));
	    top.add(trapBlue);
	    top.add(brb("xblue"));
	    top.add(trapGreen);
	    top.add(brb("xgreen"));
	    top.add(trapGray);
	    top.add(brb("xgray"));
	    top.add(trapMagenta);
	    top.add(brb("xmagenta"));
	    top.add(trapOrange);
	    top.add(brb("xorange"));
	    top.add(trapPink);
	    top.add(brb("xpink"));
	    top.add(trapRed);
	    top.add(brb("xred"));
	    top.add(trapWhite);
	    top.add(brb("xwhite"));
	    top.add(trapYellow);
	    top.add(brb("xyellow"));
	    JPanel mid = new JPanel();
	    top.add(bbt("Clear Users"));
	    if(isApplet) top.add(new Label(""));
	    else top.add(bbt("Load Users"));
	    top.add(bbt("All On"));
	    top.add(bbt("All Off"));
	    JPanel bot = new JPanel();
	    top.add(bbt("Redraw"));
	    top.add(bbt("Exit This Dialog"));
	    jw.getContentPane().add(top);
            jw.setSize(new Dimension(280, 440));
	    setCenter(jw);
            jw.setVisible(true);
	    jw.addWindowListener(new dialogClosing(new TrapActionListener("Exit This Dialog")));
	}

	public void setCenter(Component c){
		int w = gJrnlFrame.getWidth();
		int h = gJrnlFrame.getHeight();
		int x = gJrnlFrame.getX();
		int y = gJrnlFrame.getY();
		int ww = c.getWidth();
		int hh = c.getHeight();
		c.setLocation(x + ((w - ww) /2), y + ((h - hh) /2));
	}

	private void netSaveDialog(Point p, Rectangle r){
	    oldurlencoded = urlencoded;
	    oldPromptForNetSaveName = promptForNetSaveName;
	    JDialog jw = new JDialog(gJrnlFrame, "Network Save Options");
	    JPanel top = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    JPanel bot = new JPanel();
            jw.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            Container jwc = jw.getContentPane();
	    JTextArea options = new JTextArea(netOptions, 10, 30);
	    JScrollPane optionspane = new JScrollPane(options);
	    JTextField server = new JTextField(netServer);
	    JTextField saveName = new JTextField(nname);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.ipadx = 8;
	    gbc.ipady = 8;
	    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0.1;
            top.add(new JLabel(trans("Server")), gbc);
	    gbc.gridy = 1;
	    gbc.gridx = 0;
	    top.add(new JLabel(trans("Save Name")), gbc);
	    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridy = 0;
	    gbc.gridx = 2;
	    gbc.weightx = 0.1;
            JButton paste0 = new JButton(trans("Paste"));
	    paste0.addActionListener(new JrnlDialogButtonListener("paste0", server, saveName, options, jw));
	    top.add(paste0, gbc);
	    gbc.gridy = 4;
	    gbc.gridx = 2;
	    gbc.weightx = 0.1;
            JButton paste2 = new JButton(trans("Paste"));
	    paste2.addActionListener(new JrnlDialogButtonListener("paste2", server, saveName, options, jw));
	    top.add(paste2, gbc);
	    gbc.gridy = 3;
	    gbc.gridx = 2;
	    gbc.weightx = 0.1;
            //JButton enter = new JButton(trans("Enter"));
	    //enter.addActionListener(new JrnlDialogButtonListener("enter", server, saveName, options, jw));
	    //top.add(enter, gbc);
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.weightx = 1.0;
            top.add(server, gbc);
	    gbc.gridy = 1;
	    top.add(saveName, gbc);
	    gbc.gridx = 2;
	    gbc.weightx = 0.1;
            JButton paste1 = new JButton(trans("Paste"));
	    paste1.addActionListener(new JrnlDialogButtonListener("paste1", server, saveName, options, jw));
	    top.add(paste1, gbc);
	    gbc.weightx = 1.0;
	    JCheckBox urle = new JCheckBox();
	    urle.addItemListener(new JrnlDialogBoxListener("urlencoded"));
	    if(urlencoded) urle.setSelected(true);
	    JCheckBox pfnsn = new JCheckBox();
	    pfnsn.addItemListener(new JrnlDialogBoxListener("promptForNetSaveName"));
	    if(promptForNetSaveName) pfnsn.setSelected(true);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0.1;
	    top.add(new JLabel(trans("URLEncode")), gbc);
	    gbc.gridy = 2;
	    top.add(new JLabel(trans("Save Prompt")), gbc);
	    gbc.gridy = 3;
	    gbc.gridx = 1;
	    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
	    gbc.weightx = 1.0;
	    top.add(urle, gbc);
	    gbc.gridy = 2;
            top.add(pfnsn, gbc);
	    gbc.gridx = 0;
            gbc.gridy = 4;
	    gbc.anchor = GridBagConstraints.FIRST_LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0.1;
            top.add(new JLabel(trans("Variables")), gbc);
            gbc.gridx = 1;
	    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
	    gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
	    gbc.weighty = 1.0;
            top.add(optionspane, gbc);
            JButton ok = new JButton(trans("OK"));
	    ok.addActionListener(new JrnlDialogButtonListener("ok", server, saveName, options, jw));
	    JButton cancel = new JButton(trans("Cancel"));
	    cancel.addActionListener(new JrnlDialogButtonListener("cancel", server, saveName, options, jw));
            bot.setLayout(new BoxLayout(bot, BoxLayout.LINE_AXIS));
            bot.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            bot.add(Box.createHorizontalGlue());
            bot.add(ok);
            bot.add(Box.createRigidArea(new Dimension(10, 0)));
            bot.add(cancel);
            jwc.add(top, BorderLayout.CENTER);
            jwc.add(bot, BorderLayout.PAGE_END);
            if(p != null){
                int h = r.height;
                int w = r.width;
                p.y += h;
                jw.setLocation(p);
            }
            jw.setSize(new Dimension(480, 300));
	    if(p == null) setCenter(jw);
            jw.setVisible(true);
	    jw.addWindowListener(new dialogClosing(new JrnlDialogButtonListener("cancel", server, saveName, options, jw)));
	}

	public void hotkeys(){
		JOptionPane.showConfirmDialog(gJrnlFrame, "arrow move selection\nctrl+arrow extend selection\npage up\npage down\nctrl++ zoom in\nctrl+= zoom in\nctrl+- zoom out\nctrl+enter next frame\nctrl+x cut\nctrl+c copy\nctrl+v paste\nctrl+z undo\nctrl+y redo\nctrl+b bold\nctrl+i italic\nctrl+u underline\nctrl+r ruler\nctrl+d arrow\nctrl+h highlighter\nctrl+w pen\nctrl+t text\nctrl+e eraser\nctrl+f select\nctrl+a repeat last menu item\nctrl+n New\nctrl+o Open\nctrl+p Print\nctrl+q Exit\nctrl+s Save\nescape exit full screen mode", "Hot Keys", JOptionPane.DEFAULT_OPTION);
	}

	public String mymemory(){
		Runtime rt = Runtime.getRuntime();
		long mx = (long)(Jtool.maxMemory() / 1000000);
		long al = (long)(rt.totalMemory() / 1000000);
		long us = rt.totalMemory() - rt.freeMemory();
		us = (long)(us / 1000000);
		return "" + mx + "M - maximum memory\n" + al + "M - allocated memory\n" + us + "M - used memory";
	}	

	public void aboutmemory(){

		JOptionPane.showConfirmDialog(gJrnlFrame, "You may need to increase the memory allocated to your java virtual machine\nto avoid out of memory errors; use\njava -Xmx256m -jar jarnal.jar\nto run a 256 meg virtual machine. You can use a larger number if necessary.\n" + mymemory(), "Increasing Memory", JOptionPane.DEFAULT_OPTION);
	}

	public void documentation(){
		String est = firefox;
		est = Jtool.replaceAll(est, "%1", homeserver + "jarnal.htm");
		try{
			Runtime.getRuntime().exec(est);	
		}
		catch(Exception ex){System.out.println("Cannot exec " + est);}			
	}

	public void about(){
		InputStream in = Jarnal.class.getResourceAsStream("ver.txt");
		int nmax = 10000;
		byte b[] = new byte[nmax];
		String s = "";
		int nread = 0;
		try { nread = in.read(b); }
		catch (Exception e) {System.err.println(e);}
		s = new String(b,0,nread);
		String usd = "";
		if(!isApplet) {
			File userDir = new File(System.getProperty("user.home"));
			usd = "\nYour home directory is " + userDir;
		}
		JOptionPane.showConfirmDialog(gJrnlFrame, "Jarnal is a pen-centric journaling program. \nversion " + s.trim() + "\nWritten by David K. Levine, Gunnar Teege, and Gerhard Hagerer August 2003 and released under the GNU license. \nCode and documentation at\nhttp://www.dklevine.com/general/software/tc1000/jarnal.htm." + usd,    "About Jarnal", JOptionPane.DEFAULT_OPTION);
	}

	private String getConf(){
		String s = "";
		s = s + "[Globals]\n";
		s = s + "pageNumber=" + jpages.getPage() + "\n";
		float scale = nc() * jpages.getScale();
		s = s + "scale=" + scale + "\n";
		s = s + "highlightLines=" + jpages.highlightLines + "\n";
		s = s + "highlighterStyle=" + highlighterStyle + "\n";
		s = s + "fitWidth=" + fitWidth + "\n";
		s = s + "viewQuality=" + viewQuality + "\n";
		s = s + "bWidth=" + jt.bWidth + "\n";
		s = s + "hTrans=" + jt.hTrans + "\n";
		s = s + "activePage=" + activePage + "\n";
		s = s + "thumbs=" + thumbs + "\n";
		s = s + "threeup=" + threeup + "\n";
		s = s + "poverlay=" + poverlay + "\n";
		s = s + "PO=" + PO + "\n";
		s = s + jpages.getDefaultPaper();
		Dimension d = null;
		if(!embed && (gJrnlFrame != null)) d = gJrnlFrame.getSize();
		else d = frameSize;
		s = s + "windowHeight=" + d.getHeight() + "\n";
		s = s + "windowWidth=" + d.getWidth() + "\n";
		s = s + "divwidth=" + divwidth + "\n";
		s = s + "outheight=" + outheight + "\n";
		s = s + "obgcolor=" + outline.bgcolor + "\n";
		s = s + "foffX=" + jrnlPane.foffX + "\n";
		s = s + "foffY=" + jrnlPane.foffY + "\n";
		if(showOutline) s = s + "showOutline=true\n";
		if((tpanel != null) && !showOutline) s = s + "showBarJarnal=true\n";
		Point p = null;
		if(!embed && (gJrnlFrame != null)) p = gJrnlFrame.getLocation();
		else p = new Point(0, 0);
		s = s + "windowX=" + p.getX() + "\n";
		s = s + "windowY=" + p.getY() + "\n";
		s = s + "textMode=" + textMode + "\n";
		if(saveBookmarks){
			if(!netServer.equals(""))s = s + "netServer=" + netServer + "\n";
			if(!nname.equals("unsaved.jaj"))s = s + "netSaveName=" + nname + "\n";
			if(!email.equals("")) s = s + "email=" + email + "\n";
		}
		if(bpanel != null) s = s + "showInternalMini=true\n";
		s = s + "pencentric=" + pencentric + "\n";
		s = s + "saveOnExit=" + saveOnExit + "\n";
		s = s + "saveSelfexecuting=" + saveSelfexecuting + "\n";
		s = s + "minScrShot=" + mscr + "\n";
		s = s + "allScr=" + ascr + "\n";
		s = s + "saveUserInfo=" + saveBookmarks + "\n";
		s = s + "updateBookmarks=" + updateBookmarks + "\n";
		s = s + "saveBg=" + saveBg + "\n";
		s = s + "smoothStrokes=" + smoothStrokes + "\n";
		s = s + "defaultText=" + jpages.getParms() + "\n";
		s = s + "defaultOverlay=" + jrnlPane.defaultOverlay + "\n";
		s = s + "alignToMargins=" + alignToMargins + "\n";
		s = s + "bestFit=" + bestFit + "\n";
		s = s + "absoluteScale=" + absoluteScale + "\n";
		s = s + "showPageNumbers=" + showPageNumbers + "\n";
		s = s + "withBorders=" + withBorders + "\n";
		s = s + "URLEncode=" + urlencoded + "\n";
		s = s + "promptForNetSaveName=" + promptForNetSaveName + "\n";
		s = s + "stickyRuler=" + stickyRuler + "\n";
		s = s + "arrowhead=" + arrowhead + "\n";
		s = s + "markerweight=" + markerweight + "\n";
		s = s + "middleButton=" + middleButton + "\n";
		s = s + "rightButton=" + rightButton + "\n";
		s = s + "\n";
		s = s + "[Default Pen]\n" + jtd.getConf() + "\n";
		s = s + "[Default Highlighter]\n" + jth.getConf() + "\n";
		s = s + "[Button Pen]\n" + jtbu.getConf() + "\n";
		s = s + "[Current Tool]\n" + jt.getConf() + "\n";
		s = s + "[Toolbar1]\n" + tb1.trim() + "\n\n";
		s = s + "[Toolbar2]\n" + tb2.trim() + "\n\n";
		s = s + "[Toolbar3]\n" + tb3.trim() + "\n\n";
		if(saveBookmarks){
			s = s + "[Bookmarks]\n" + getBookmarks() + "\n";
			s = s + "[Servermarks]\n" + getServermarks() + "\n";
		}
		if(!netOptions.trim().equals("") && saveBookmarks){
			s = s + "[Net Options]\n" + netOptions + "\n\n";
		}
		s = s + jpages.saveBgsList(cwd);
		if(usersList.size() != 0) s = s + "[Users List]\n" + Jtool.writeConf(usersList) + "\n\n";
		s = s + jpages.getFindConf() + "\n";
		return s;
	}

	
	//new fitwidth: has been changed to the a default of false for existing files
	public void setDefaultConf(){
		setConf(defaultConf);
		//setConf("[Globals]\nx=0\ny=0\nhighlighterStyle=translucent\npageNumber=1\nscale=0.97759104\nhighlightLines=false\nfitWidth=false\npaper=Lined\nlines=25\nheight=861\nwidth=714\nwindowHeight=992.0\nwindowWidth=738.0\ndivwidth=150\noutheight=90\nshowOutline=false\n\foffX=0\nfoffY=0\nobgcolor=blue\ntextMode=false\npencentric=false\nsaveOnExit=false\nsaveSelfexecuting=false\nminScrShot=true\nallScr=false\nviewQuality=64\nbWidth=2.2\nhTrans=1.0\nactivePage=0\npoverlay=false\nPO=0.25\nthumbs=true\nthreeup=true\npromptForNetSaveName=false\nstickyRuler=false\nsaveBookmarks=false\nupdateBookmarks=true\nsaveBg=false\ndefaultText=font-family=\"Vera\" font-size=\"20\" fill=\"black\"\ndefaultOverlay=" + Jtool.defaultOverlay + "\nalignToMargins=true\nbestFit=true\nshowPageNumbers=true\nURLEncode=false\nnetSaveName=unsaved.jaj\nwithBorders=true\n\n[Default Pen]\ntype=Medium\ncolor=black\nhighlighter=false\n\n[Default Highlighter]\ntype=Fat\nfatWidth=11.0\ncolor=green\nhighlighter=false\transparency=100\n\n[Current Tool]\ntype=Medium\ncolor=black\nhighlighter=false\n\n[Toolbar1]\n" + dtb1 + "\n[Toolbar2]\n" + dtb2 + "\n");
		netServer="";
		netOptions="";
		email = "";
	}

	public void setConf(String s){
		String z;

		s = s + "\n";

		String y = Jtool.getEntry(s, "[Globals]");
		jpages.setDefaultPaper(y);
		z = Jtool.getLine(y, "pageNumber");
		if(z != null){
			int p = Integer.parseInt(z);
			if(p > 1) jpages.nextPage(p - 1);
			activePage = p - 1;
		}
		z = Jtool.getLine(y, "viewQuality");
		if(z != null){
			viewQuality = Integer.parseInt(z);
			setVQ();
		}
		z = Jtool.getLine(y, "bWidth");
		float zbWidth = jth.bWidth;
		if(z != null) {
			jth.bWidth = Float.parseFloat(z);
			zbWidth = jth.bWidth;
			jtd.bWidth = jth.bWidth;
			jtbu.bWidth = jth.bWidth;
			jt.bWidth = jth.bWidth;
			jpages.jtool.bWidth = jth.bWidth;
		}
		z = Jtool.getLine(y, "hTrans");
		float zhTrans = jth.hTrans;
		if(z != null) {
			jth.hTrans = Float.parseFloat(z);
			zhTrans = jth.hTrans;
			jtd.hTrans = jth.hTrans;
			jtbu.hTrans = jth.hTrans;
			jt.hTrans = jth.hTrans;
			jpages.jtool.hTrans = jth.hTrans;
		}
		z = Jtool.getLine(y, "PO");
		if(z != null) {
			PO = Float.parseFloat(z);
		}
		z = Jtool.getLine(y, "saveBg");
		if(z != null) {
			if(z.equals("true")) saveBg = true;
			sbg.setState(saveBg);
			sbg2.setState(saveBg);
			jpages.saveBg = saveBg;
		}
		z = Jtool.getLine(y, "smoothStrokes");
		if(z != null) {
			if(z.equals("true")) smoothStrokes = true;
		}
		smstrk.setState(smoothStrokes);
		z = Jtool.getLine(y, "allScr");
		if(z != null){
			if(z.equals("true")) ascr = true;
			else ascr = false;
			ascr1.setState(ascr);
			ascr2.setState(ascr);
			ascr3.setState(ascr);
		}
		z = Jtool.getLine(y, "minScrShot");
		if(z != null){
			if(z.equals("true")) mscr = true;
			else mscr = false;
			scr1.setState(mscr);
			scr2.setState(mscr);
			scr3.setState(mscr);
		}
		z = Jtool.getLine(y, "backgroundFile");
		if(z != null) {
			bgfile = z;
			//the next line should only be executed only initial program load
			//not if we are subsequently resetting the conf
			//or if we do reset the conf, it should at least be an undoable operation
			jpages.initOpenBg(new JbgsSource(bgfile,null));
		}
		z = Jtool.getLine(y, "highlightLines");
		if(z != null){
			if(z.equals("true")) jpages.highlightLines = true;
			else jpages.highlightLines = false;
		}
		z = Jtool.getLine(y, "highlighterStyle");
		if(z != null) {
			highlighterStyle = z;
		}
		z = Jtool.getLine(y, "thumbs");
		if(z != null) {
			if(z.equals("true")) thumbs = true;
			else thumbs = false;
		}
		z = Jtool.getLine(y, "threeup");
		if(z != null) {
			if(z.equals("true")) threeup = true;
			else threeup = false;
		}
		z = Jtool.getLine(y, "poverlay");
		if(z != null) {
			if(z.equals("true")) {
				poverlay = true;
				jpages.PO = PO;
			}
			else {
				poverlay = false;
				jpages.PO = 2.0;
			}
		}
		z = Jtool.getLine(y, "activePage");
		//if(z != null) activePage = (int) Float.parseFloat(z);		
		z = Jtool.getLine(y, "scale");
		if(z != null) {
			jpages.invalidateGraphics();
			jpages.setScale(Float.parseFloat(z) / nc());
		}
		z = Jtool.getLine(y, "fitWidth");
		if(z != null){
			if(z.equals("true")) fitWidth = true;
			else fitWidth = false;
		}
		z = Jtool.getLine(y, "windowWidth");
		if(z != null){
			int w = (int) Float.parseFloat(z);
			z = Jtool.getLine(y, "windowHeight");
			if(z != null){
				int h = (int) Float.parseFloat(z);
				frameSize = new Dimension(w, h);
			}
		}
		z = Jtool.getLine(y, "windowX");
		if(z != null){
			int xx = (int) Float.parseFloat(z);
			z = Jtool.getLine(y, "windowY");
			if(z != null){
				int yy = (int) Float.parseFloat(z);
				if(!locationSet) frameLocation = new Point(xx, yy);
			}
		}
		z = Jtool.getLine(y, "email");
		if(z != null){
			email = z;
		}
		z = Jtool.getLine(y, "netServer");
		if(z != null) {
			netServer = z;
		}
		z = Jtool.getLine(y, "URLEncode");
	        if(z != null){
			if(z.equals("true")) urlencoded = true;
			else urlencoded = false;
	        }
		z = Jtool.getLine(y, "markerweight");
		if(z != null){
			markerweight = Integer.parseInt(z);
		}
		z = Jtool.getLine(y, "arrowhead");
		if(z != null){
			if(z.equals("true")) arrowhead = true;
			else arrowhead = false;
			arcb.setState(arrowhead);
		}
		z = Jtool.getLine(y, "stickyRuler");
		if(z != null){
			if(z.equals("true")) stickyRuler = true;
			else stickyRuler = false;
			srcb.setState(stickyRuler);
		}
		z = Jtool.getLine(y, "middleButton");
		if(z != null) middleButton = z;
		z = Jtool.getLine(y, "rightButton");
		if(z != null) rightButton = z;
		z = Jtool.getLine(y, "netSaveName");
		if(z != null) nname = z;
		z = Jtool.getLine(y, "promptForNetSaveName");
		if(z != null){
			if(z.equals("true")) promptForNetSaveName = true;
			else promptForNetSaveName = false;
			pfnsn.setState(promptForNetSaveName);
		}
		z = Jtool.getLine(y, "saveOnExit");
		if(z != null) {
			if(z.equals("true")) saveOnExit = true;
			soe.setState(saveOnExit);
		}
		z = Jtool.getLine(y, "saveSelfexecuting");
		if(z != null) {
			if(z.equals("true")) saveSelfexecuting = true;
			jcbSaveSelfexecuting.setState(saveSelfexecuting);
		}
		z = Jtool.getLine(y, "saveBookmarks");
		if(z != null) {
			if(z.equals("true")) saveBookmarks = true;
			sbmk.setState(saveBookmarks);
		}
		z = Jtool.getLine(y, "saveUserInfo");
		if(z != null) {
			if(z.equals("true")) saveBookmarks = true;
			sbmk.setState(saveBookmarks);
		}
		z = Jtool.getLine(y, "updateBookmarks");
		if(z != null) {
			if(z.equals("false")) updateBookmarks = false;
			abmk.setState(updateBookmarks);
		}
		z = Jtool.getLine(y, "foffX");
		if(z != null) {
			jrnlPane.foffX = Integer.parseInt(z);
		}
		z = Jtool.getLine(y, "foffY");
		if(z != null) {
			jrnlPane.foffY = Integer.parseInt(z);
		}
		z = Jtool.getLine(y, "obgcolor");
		if(z != null) {
			outline.bgcolor = z;
		}
		z = Jtool.getLine(y, "outheight");
		if(z != null) {
			outheight = Integer.parseInt(z);
		}
		z = Jtool.getLine(y, "showOutline");
		if(z != null){
			startOutline = true;
		}
		z = Jtool.getLine(y, "divwidth");
		if(z != null) {
			divwidth = Integer.parseInt(z);
		}
		z = Jtool.getLine(y, "showBarJarnal");
		if(z != null){
			startBarJarnal = true;
		}
		z = Jtool.getLine(y, "showInternalMini");
		if(z != null){
			startInternalMini = true;
		}
		z = Jtool.getLine(y, "pencentric");
		if(z != null){
			if(z.equals("true")){
				pencentric = true;
				pencen.setSelected(pencentric);
			}
		}
		z = Jtool.getLine(y, "textMode");
		if(z != null){
			if(z.equals("true")) textMode = true;
		}
		z = Jtool.getLine(y, "defaultText");
		if(z != null) jpages.setParms(z);

		z = Jtool.getLine(y, "defaultOverlay");
		if(z != null) jrnlPane.defaultOverlay = z;

		z = Jtool.getLine(y, "alignToMargins");
		if(z != null){
			if(z.equals("false")) alignToMargins = false;
		}
		pam.setState(alignToMargins);

		z = Jtool.getLine(y, "bestFit");
		if(z != null){
			if(z.equals("false")) bestFit = false;
		}
		pbf.setState(bestFit);

		z = Jtool.getLine(y, "absoluteScale");
		if(z != null){
			if(z.equals("true")) absoluteScale = true;
		}
		pas.setState(absoluteScale);

		z = Jtool.getLine(y, "showPageNumbers");
		if(z != null){
			if(z.equals("false")) showPageNumbers = false;
		}
		psp.setState(showPageNumbers);

		z = Jtool.getLine(y, "withBorders");
		if(z != null){
			if(z.equals("true")) withBorders = true;
		}
		wbr.setState(withBorders);

		y = Jtool.getEntry(s, "[Bookmarks]");
		setMarks(y, BOOKMARKS);

		y = Jtool.getEntry(s, "[Servermarks]");
		setMarks(y, SERVERS);

		y = Jtool.getEntry(s, "[Users List]");
		if(y != null) usersList = Jtool.readConf(y);
		
		Jtool jtt = Jtool.getTool(Jtool.getEntry(s, "[Default Pen]"));
		if(jtt != null) jtd = jtt;
		jtt = Jtool.getTool(Jtool.getEntry(s, "[Default Highlighter]"));
		if(jtt != null) jth = jtt;
		jtt = Jtool.getTool(Jtool.getEntry(s, "[Button Pen]"));
		if(jtt != null) jtbu = jtt;
		jtt = Jtool.getTool(Jtool.getEntry(s, "[Current Tool]"));
		if(jtt != null) jt = jtt;

		fatWidth = jth.fatWidth;
		jtd.fatWidth = jth.fatWidth;
		jtbu.fatWidth = jth.fatWidth;
		jt.fatWidth = jth.fatWidth;
		jth.bWidth = zbWidth;
		jtd.bWidth = zbWidth;
		jtbu.bWidth = zbWidth;
		jt.bWidth = zbWidth;
		jth.setWidth(jth.type);
		jtd.setWidth(jtd.type);
		jtbu.setWidth(jtbu.type);
		jt.setWidth(jt.type);
		jpages.jtool.bWidth = zbWidth;
		jpages.jtool.fatWidth = jth.fatWidth;
		jpages.jtool.setWidth(jpages.jtool.type);

		jth.hTrans = zhTrans;
		jtd.hTrans = zhTrans;
		jtbu.hTrans = zhTrans;
		jt.hTrans = zhTrans;
		jpages.jtool.hTrans = zhTrans;

		z = Jtool.getEntry(s, "[Find Strings]");
		if(z != null) jpages.setFindConf(z);

		z = Jtool.getOnlyEntry(s, "[Toolbar1]");
		if(z != null) tb1 = z.trim() + "\n";

		z = Jtool.getOnlyEntry(s, "[Toolbar2]");
		if(z != null) tb2 = z.trim() + "\n";

		z = Jtool.getOnlyEntry(s, "[Toolbar3]");
		if(z != null) tb3 = z.trim() + "\n";

		z = Jtool.getEntry(s, "[Net Options]");
		if(z != null) {
			int pos = z.indexOf("\n");
			netOptions = z.substring(pos + 1);
		}
	}

	public void setMeta(){
		String netOptions2 = setMeta2(meta2) + setMeta2(meta);
		if(!netOptions2.trim().equals("")) netOptions = netOptions2;
	}

	public String setMeta2(String s){
		String z;
		s = s + "\n";
		String y = Jtool.getEntry(s, "[Globals]");
		z = Jtool.getLine(y, "netServer");
		if(z != null) {
			netServer = z;
		}
		z = Jtool.getLine(y, "netSaveName");
		if(z != null) nname = z;
		z = Jtool.getLine(y, "URLEncode");
	        if(z != null){
			if(z.equals("true")) urlencoded = true;
			else urlencoded = false;
		}
		z = Jtool.getLine(y, "promptForNetSaveName");
		if(z != null) {
			if(z.equals("true"))promptForNetSaveName = true;
			else promptForNetSaveName = false;
		}
		String netOptions2 = null;
		netOptions2 = Jtool.getEntry(s, "[Net Options]");
		if(netOptions2 != null) {
			int pos = netOptions2.indexOf("\n");
			netOptions2 = netOptions2.substring(pos + 1);
		}
		else netOptions2 = "";
		return netOptions2;
	}

	public void notImplemented(){
		JOptionPane.showConfirmDialog(gJrnlFrame, "Option not implemented",    "Fix me", JOptionPane.DEFAULT_OPTION);
	}

	public boolean winDone(){
		if(mini) return true;
		if(saveOnExit && dirty){
			if(!isNetSave) jrnlPane.doAction("Save No Dialog");
			else jrnlPane.doAction("Net Save");
		}
		int n = 2;
		if(dirty) n = (new Jarnbox(gJrnlFrame, "Confirm Exit")).showSaveExitCancel();
		//if(dirty) n = JOptionPane.showConfirmDialog(gJrnlFrame, "All unsaved data will be lost. OK to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
		if(n == 1){
			if(!isNetSave) jrnlPane.doAction("Save and Exit");
			else jrnlPane.doAction("Network Save and Exit");
		}
		if(n == 2) return true;
		return false;
	}	

	private String suggestName(){
		GregorianCalendar cal = new GregorianCalendar();
		String sug = "" + cal.get(Calendar.YEAR) + "_" + fixDate(cal.get(Calendar.MONTH) - Calendar.JANUARY + 1) + "_" + fixDate(cal.get(Calendar.DATE)) + "_" + fixDate(cal.get(Calendar.HOUR_OF_DAY)) + "_" + fixDate(cal.get(Calendar.MINUTE)) + "_" + fixDate(cal.get(Calendar.SECOND));
		return sug;
	}	

	public String getFile(String type, boolean setFilter){
		JFileChooser fd = new JFileChooser(cwd);
		if(setFilter) fd.setFileFilter(new ffilter());
		if(type.startsWith("Open") || type.startsWith("Insert")) {
			int test = fd.showDialog(gJrnlFrame, type);
			if(test == JFileChooser.CANCEL_OPTION) return null;
		}
		else {
			String sug = suggestName();
			String zext = ext;
			if(type.equals("Save Html")) zext = ".htm";
			if(type.equals("Save Text")) zext = ".txt";
			if(type.equals("Save Snapshot")) zext = ".jpg";
			if(type.equals("Print as PDF")) zext = ".pdf";
			if(type.equals("Save TIFF")) zext = ".tif";
			if(!fname.equals("")) sug = fname + "." + sug;
			else{
				bgfile = jpages.bgs().getSource().getName();
				if(bgfile == null) bgfile = "";
				if(!bgfile.equals("")) sug = bgfile + "." + sug;
			}
			sug = sug + zext;
			if(type.equals("Save Text") && (textfile != null)) sug = textfile;
			if(pencentric){
				String ans = (new Jarnbox(gJrnlFrame, "Save Name", jarn, true)).getString(sug);
				if(ans != null) sug = ans;
			}
			fd.setSelectedFile(new File(sug));
			int test = fd.showDialog(gJrnlFrame, type);
			if(test == JFileChooser.CANCEL_OPTION) return null;
		}
		//String temp = fd.getName(fd.getSelectedFile());
		String temp = fd.getSelectedFile().getName();
		if (temp == null) return null;
		File tdir = fd.getCurrentDirectory();
		cwd = tdir.getPath();			
		return temp;
	}

	public void setStop(){
		if(!jpages.wantscontrol){
			jpages.jcom.requestinactive();
			return;
		}
		if(!replayActive){
			locked = false;
			dragOp = 0;
		}
		handButton.setToolTipText(trans("Release Control"));
		handButton.setIcon(handstop);
		handButton.setVisible(true);
		jpages.active = true;
	}

	public void setWarning(){
		if(handButton.getIcon() != handmixed) handButton.setIcon(handmixed2);
	}

	public void setStart(){
		locked = true;
		dragOp = 100;
		handButton.setToolTipText(trans("Request Control"));
		handButton.setIcon(hand);
		handButton.setVisible(true);
		jpages.active = false;
		jpages.wantscontrol = false;
	}

	public void doDisconnect(){
		if(jpages.jcom != null){
			jpages.jcom.disconnect();
			jpages.jcom = null;
		}
		setConnectMenu(false);
		if(handButton != null) handButton.setVisible(false);
		disconnectServer.setVisible(false);
		disconnectActiveClient.setVisible(false);
		startServer.setVisible(true);
		connectServer.setVisible(true);
		srv.setVisible(true);
		dsrv.setVisible(true);
		allsrv.setVisible(true);
		if(!replayActive){
			locked = false;
			dragOp = 0;
		}
		jpages.active = true;
		serverMsg = "";
		setStatus("");
	}

	public void setDisconnect(){
		startServer.setVisible(false);
		disconnectServer.setVisible(true);
	}

	public String getDate(){
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("EEE MMM d, yyyy h:mm a");
		Date now = new Date();
		return df.format(now);
	}

	public void loadToolbar(int tbar){
		String oldcwd = cwd;
		String temp = getFile("Open Toolbar " + tbar, false);
		String uwd = cwd;
		cwd = oldcwd;
		String userfile = uwd + File.separator + temp;
		String instr = null;
		try{
			instr = new String(jpages.streamToByteArray(new FileInputStream(userfile)));
		}
		catch(Exception ex){System.err.println(ex); instr = null;}
		if(instr != null){
			if(tbar == 1) setToolbars(instr, null);
			if(tbar == 2) setToolbars(null, instr);
			if(tbar == 3) tb3 = instr;
		}
	}

	public void setToolbars(String ntb1, String ntb2){
		if((ntb1 == null) && (ntb2 == null)){
			ntb1 = dtb1;
			ntb2 = dtb2;
		}
		if(ntb1 != null) {
			tb1 = ntb1;
		}
		if(ntb2 != null) {
			tb2 = ntb2;
		}
		if((ntb1 != null) || (ntb2 != null)){
			jtb1.removeAll();
			jtb2.removeAll();
			RQ = false;
			SB = false;
			parseTB(jtb1, tb1);
			parseTB(jtb2, tb2);
			jtb1.repaint();
			jtb2.repaint();
			dirty = true;
		}
	}

	JFrame fscr = null;
	JrnlScrollListener jsl = new JrnlScrollListener();

	public void toFullScreen(){
		clockTimer.start();
		addMouseWheelListener(jsl);
		jpages.showTextMarks(false);
		fscr = new JFrame();
		fscr.setUndecorated(true);
		fscr.setResizable(false);
		Container container = fscr.getContentPane();
		boolean showHand = handButton.isShowing();
		jtb1.removeAll();
		RQ = false;
		SB = false;
		if(parseTB(jtb1, tb3)) container.add(jtb1, BorderLayout.SOUTH);
		if(showHand) handButton.setVisible(true);
		sp.setViewportView(null);
		if(showOutline){
			Container cp = gJrnlFrame.getContentPane();
			cp.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
			JPanel jpa = new JPanel(new BorderLayout());
			outline.initOut(jpa);
			outline.setFullScreen(true);
			tpanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpa, this);
			tpanel.setDividerSize(1);
			tpanel.setDividerLocation(outheight);
			container.add(tpanel, BorderLayout.CENTER);
		}
        	else container.add(this, BorderLayout.CENTER);
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fscr);
	}

	public void fromFullScreen(){
		removeMouseWheelListener(jsl);
		jpages.showTextMarks(true);
		boolean showHand = handButton.isShowing();
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		fscr.setVisible(false);
		sp.setViewportView(this);
		jtb1.removeAll();
		jtb2.removeAll();
		RQ = false;
		SB = false;
		if(parseTB(jtb1, tb1)) jpt.add(jtb1, BorderLayout.NORTH);
		if(parseTB(jtb2, tb2)) jpt.add(jtb2, BorderLayout.SOUTH);
		if(showHand) handButton.setVisible(true);
		if(showOutline){
			Container container = fscr.getContentPane();
			Container cp = gJrnlFrame.getContentPane();
			container.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
			JPanel jpa = new JPanel(new BorderLayout());
			outline.initOut(jpa);
			outline.setFullScreen(false);
			tpanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpa, sp);
			tpanel.setDividerLocation(outheight);
			cp.add(tpanel, BorderLayout.CENTER);
		}
		offX = 0;
		offY = 0;
		jpt.repaint();
	}

	void opentext(){
		String instr = "";
		try{
			FileInputStream fis = new FileInputStream(cwd + File.separator + textfile);
			instr = new String(jpages.streamToByteArray(fis));
			fis.close();
		}
		catch(Exception ex){System.out.println("File: " + textfile + " not found"); return;}
		jpages.setStartMark();
		jpages.reText();
		textOp("type", 0, instr);
		jpages.textSplit();
		int h = (int) jpages.getHeight();
		Rectangle rr = jpages.forceTextRectangle();
		int y = (int) rr.getY();
		int t = h - (2 * y);
		if(t <= y) t = (int)(0.9 * (h - y));
		if(t <= 0) t = (int)(0.1 * h);
		jpages.setTextHeight(y + t);
		drawState = 0;
		jpages.setEndMark();
		dirty = true;
	}

	public void open(){
		String obgfile = bgfile;
		boolean setSizeToBg = false;
		if(!openfile.equals("")) {
			File temp = new File(openfile);
			if(template) templateFile = openfile;
			if(!template){
				fname = temp.getName();
				cwd = temp.getParent();
			}
			boolean doOpen = true;
			if(!isApplet){
				doOpen = temp.exists();
			}
			if(doOpen){
				setConf(jpages.open(openfile));
				addBookmarkAll(openfile);
				if(jpages.recordingOn()) recbox.setState(true);
				//new fitwidth: keep the scale on an existing file
				fitWidth = false;
			}
			else gJrnlFrame.setTitle("Jarnal");
		}
		else setSizeToBg = true;
		if(template && (jpages.getPages() == 1)) setSizeToBg = true;
		openfile = "";
		if(!obgfile.equals("")){
			jpages.openBg(new JbgsSource(obgfile, null));
			obgfile = "";
			//new fitwidth: if we load a background set the width to the background size
			fitWidth = true;
		}
		if(!opentextfile.equals("")){
			File temp = new File(opentextfile);
			textfile = temp.getName();
			cwd = temp.getParent();
		}
		jpages.openLoad(loadFiles);
		loadFiles = new LinkedList();
		template = false;
		if(!savefile.equals("")){
			File temp = new File(savefile);
			fname = temp.getName();
			cwd = temp.getParent();
			savefile = "";
		}
	}

	public void getNetSaveName(){
		String sugname = nname;
		if(sugname.equals("unsaved.jaj")) sugname = suggestName();
		String ans = (new Jarnbox(gJrnlFrame, "Network Save Name", jarn, true)).getString(sugname);
		if(ans != null) nname = ans;
	}

	public Jarnal openName(String cwdt, String temp){
		if(embed) return null;
		if(temp != null) {
			jpages.invalidateGraphics();
			openfile = cwdt + File.separator + temp;
			Jarnal newJ = Jarnal.newJarnal("Jarnal - " + temp);
			if((fname == "") && !dirty){
				gJrnlFrame.setVisible(false);
				checkClose();
			}
			return newJ;
		}
		return null;
	}

	private void saveDics(){
		String names[] = {"base", "num", "sym", "cap", "user"};
		String files[] = {"jarnal.recog", "jarnal_num.recog", "jarnal_sym.recog", "jarnal_cap.recog", "jarnal_user.recog"};
		File userDir = new File(System.getProperty("user.home"));
		userDir = new File(userDir, ".jarnal");
		if(!userDir.exists()) userDir.mkdir();
		for(int i = 0; i < names.length; i++){
			jpages.saveDic((new File(userDir, files[i])).getPath(), names[i]);
		}			
	}
	
	private InputStream getDicStream(String fname, String internalName){
		FileInputStream fis = null;
		if(!isApplet){ 
			File userDir = new File(System.getProperty("user.home"));
			userDir = new File(userDir, ".jarnal");
			File userDic = new File(userDir, fname);
			try{
				fis = new FileInputStream(userDic);
			}
			catch(Exception ex){fis = null;}
		}
		InputStream ven = (InputStream) fis;
		if(ven == null) {
			System.out.println("Cannot find user dictionary: " + fname);
			ven = Jarnal.class.getResourceAsStream(internalName);
		}	
		return ven;
	}

	private boolean regularDic = false;

	public void janalyzeinit(){
		if(miniDic) return;
		miniDic = true;
		InputStream in = Jarnal.class.getResourceAsStream("images/pairs.txt");
		InputStream din = Jarnal.class.getResourceAsStream("images/dict.txt");
		InputStream ven = getDicStream("jarnal.recog", "images/jarnal.recog");
		Janalyze.initStream(in, ven, din);
		ven = getDicStream("jarnal_num.recog", "images/jarnal_num.recog");
		Janalyze.initDictionary(ven, "num");
		ven = getDicStream("jarnal_sym.recog", "images/jarnal_sym.recog");
		Janalyze.initDictionary(ven, "sym");
		ven = getDicStream("jarnal_cap.recog", "images/jarnal_cap.recog");
		Janalyze.initDictionary(ven, "cap");
		ven = getDicStream("jarnal_user.recog", "images/jarnal_user.recog");
		Janalyze.initDictionary(ven, "user");	
	}

	public void setWidth(String width){
		jt.setWidth(width);
		setStatus("");
	}

	private void deletePages(){
		jpages.setStartMark();
		Iterator ts = pageList.iterator();
		while(ts.hasNext()){
			int ii = -((Integer)ts.next()).intValue();
			int pp = jpages.getPage() - 1;
			int delta = ii - pp;
			if(pp != 0) jpages.nextPage(delta);
			jpages.pageDelete();
		}
		jpages.setEndMark();
		setup();
		dirty = true;
	}

	private boolean insBgTextPages(){
		if(pageList.isEmpty()) return insBgText(true);
		boolean something = false;
		jpages.setStartMark();
		Iterator ts = pageList.iterator();
		while(ts.hasNext()){
			int ii = -((Integer)ts.next()).intValue();
			int pp = jpages.getPage() - 1;
			int delta = ii - pp;
			if(pp != 0) jpages.nextPage(delta);
			if(insBgText(false)) something = true;
		}
		jpages.setEndMark();
		return something;
	}

	private boolean insBgText(boolean mark){
		String src = jpages.getBgsName();
		if(src == null) return false;
		if(src.equals("")) return false;
		int p = jpages.bgindex() + 1;
		String data = jpages.getBgText(p, src);
		if(data == null) return false;
		if(mark) jpages.setStartMark();
		jpages.bgFade(75);
		//textOp("style", 10, "Size");
		jpages.bgText(true);
		jpages.reText();
		textOp("type", 0, data);
		drawState = 0;
		if(mark) jpages.setEndMark();
		return true;
	}	

	public BufferedImage scr = null;
	public String pdfFile = null;
	private PrinterJob pj;
	private JPanel bpanel;
	public JSplitPane tpanel;
	public boolean middleIgnore;

	public void doAction(String action){

	    //if(action.equals("Insert Background")) action = "Open Background";
	    if(action.equals("Insert Page After")) action = "New Page";
	    if(action.equals("Insert Page Before")) action = "New Page Before";

	    middleIgnore = true;
	    boolean zmiddleIgnore = false;
	    if(mini) action = miniAction(action);

	    if(!action.startsWith("xx"))lastAction = action;
	    else action = action.substring(2);
	    actionMsg = "";

	    jrnlPane.requestFocus();

	    if(action.equals("Browse")){
		if(dragOp == 200) dragOp = 0;
		else dragOp = 200;
		setCursor();
	    }

	    //if((dragOp == -1) && action.equals("Ruler")) action = "no big deal";

	    if(locked) dragOp = 100;
	    boolean gotool = true;
	    if((dragOp != 100) && (dragOp != 11) && (dragOp != 200)) {
		if(!stickyRuler && (dragOp == -1)) {
			dragOp = 0;
			action = "no big deal";
		}
		else gotool = false;
		setCursor();
	    }
	    jrnlPane.setStatus("");
	    drawState = 0;

	    int pageChange = 0;
	    boolean makeInvalid = true;

	    if(action.startsWith("Scroll")){
		if(fullScreen){
			if(action.endsWith("Down")) action = "Next Page";
			if(action.endsWith("Up")) action = "Previous Page";
		}
		else{
			JViewport jvp = sp.getViewport();
			int nscroll = sp.getVerticalScrollBar().getUnitIncrement(1);
			if(action.endsWith("Up")) nscroll = -nscroll;
			Point foosterp = jvp.getViewPosition();
			nscroll = (int) foosterp.getY() + nscroll;
			if(nscroll < 0) nscroll = 0;
			Dimension dimm = getSize();
			Rectangle r = jvp.getViewRect();
			if(r.getHeight() + nscroll > dimm.getHeight()) nscroll = (int) dimm.getHeight() - (int) r.getHeight();
			foosterp = new Point((int)foosterp.getX(), nscroll);
			jvp.setViewPosition(foosterp);
		}
	    }		

	    if(action.equals("Previous Page")) pageChange = -1;
	    if(action.equals("Next Page")) pageChange = 1;
	    if(action.equals("First Page")) pageChange = -1000;
	    if(action.equals("Last Page")) pageChange = 1000;
	    if(action.equals("Go To Page")){
		int cp = jpages.getPage();
		(new Jarnbox(gJrnlFrame, "GoToPage", jarn, false)).getInt(cp, 1000, true);

		//Number test = (new Jarnbox(gJrnlFrame, "GoToPage")).getInt(cp,1000);
	    }
	    if(action.equals("GoToPage")){
		if(gotopage != null){
			int cp = jpages.getPage();
		//if(test != null) {
		//	pageChange = test.intValue() - cp;
		//}
			pageChange = gotopage.intValue() - cp;
		}
	    }
	    if(pageChange != 0){
		if(jpages.nextPage(pageChange) && action.equals("Next Page")) action = "New Page";
		if(showOutline) outline.synchPage(pageChange);
		setTSize();
		setup();
		makeInvalid = false;
		zmiddleIgnore = true;
	    }
	    if(action.startsWith("pageref")){
		int initP = jpages.getPage();
		if(jpages.gotoPage(action)){
			if(showOutline) outline.synchPage(jpages.getPage() - initP);
			setTSize();
			setup();
			makeInvalid = false;
		}
	    }
	    if(action.startsWith("Zpageref")){
		action = action.substring(1);
		if(jpages.gotoPage(action)){
			setTSize();
			setup();
			makeInvalid = false;
		}
	    }
	    if(action.equals("Overlay Pages")){
		dirty = true;
		if(poverlay) {
			poverlay = false;
			jpages.PO = 2.0;
		}
		else {
			poverlay = true;
			jpages.PO = PO;
		}
		if(!locked) dragOp = 0;
		drawState = 0;
		jpages.invalidateGraphics();
		setup();
	    }
				
	    if(action.equals("Thumbs")){
		dirty = true;
		if(thumbs && !threeup){
			thumbs = false;
			if(!locked) dragOp = 0;
			drawState = 0;
			jpages.invalidateGraphics();
			jpages.setScale(2 * jpages.getScale());
		}
		else {
			thumbs = true;
			jpages.invalidateGraphics();
			jpages.setScale(jpages.getScale()/2);
			if(threeup){
				threeup = false;
			}
			activePage = jpages.getPage() - 1;
		}
		setup();
	    }

	    if(action.equals("Continuous")){
		dirty = true;
		if(threeup){
			thumbs = false;
			threeup = false;
			poverlay = false;
			jpages.PO = 2.0;
			if(!locked) dragOp = 0;
			drawState = 0;
		}
		else{
			if(thumbs) {
				jpages.invalidateGraphics();
				jpages.setScale(2 * jpages.getScale());
			}
			thumbs = true;
			threeup = true;
			activePage = jpages.getPage() - 1;
		}
		setup();
	    }

	    if(action.equals("Pencentric")){
		pencentric = !pencentric;
		pencen.setSelected(pencentric);
	    }

	    if(action.equals("Request Control")){
		if(jpages.jcom == null) return;
		if((!jpages.active) && jpages.wantscontrol){
			setStart();
		}
		else if(!jpages.active) {
			handButton.setIcon(handmixed);
			handButton.setToolTipText(trans("Cancel Control Request"));
			jpages.wantscontrol = true;
			jpages.jcom.requestactive();
		}
		else {
			jpages.putdo(true);
			jpages.jcom.requestinactive();
			setStart();
		}
	    	return;
	    }

	    if(action.equals("Disconnect")){
		doDisconnect();
		return;
	    }

	    if(action.equals("Disconnect Active Client")){
		jserver.activeclientremove();
	    	return;
	    }

	    if(action.equals("Adjust Time Zone")){
		Number test = (new Jarnbox(gJrnlFrame, "Increase Hours By")).getInt(tzadjust,-24, 24, false);
		if(test == null) return;
		tzadjust = test.intValue();
		writeShell();
		return;
	    }

	    if(action.equals("Start Server")){
		Number test = (new Jarnbox(gJrnlFrame, "Server Port", jarn, true)).getInt(defaultServerPort,0, 10000, false, true);
		if(test == null) return;
		serverPort = test.intValue();
		jserver= new JarnalServer(jarn, serverPort);
		jserver.start();
		setStart();
		setDisconnect();	
		connectServer.setVisible(false);
		srv.setVisible(false);
		dsrv.setVisible(false);
		allsrv.setVisible(false);
		disconnectActiveClient.setVisible(true);
		setConnectMenu(true);
		setStatus("");	
		return;
	    }

	    String server = null;

	    if(action.length() >= 9){
		if(action.substring(0, 8).equals("server:/")){
			server = action.substring(8);
			action = "Connect to Server";
		}
	    }

	    if(action.equals("Connect to Server")){
		if(server == null) server = (new Jarnbox(gJrnlFrame, "Server", jarn, true)).getString("localhost");
		if(server == null) return;
		addServerAll(server);
		JarnalClient jcom = new JarnalClient(null, jarn, server, null);
		File tfile = null;
		try{
			tfile = jcom.getFile();
		}
		catch(Exception ex){System.err.println("Can't get temp file " + ex);}
		if(tfile != null) {
			Jcom jc = jpages.jcom;
			jpages.jcom = null;
			//we must get jcom out of jpages before doing openName
			//open name can close the existing Jarnal, and this will cause
			//jpages to kill the connection
			Jarnal newJ = openName(tfile.getParent(), tfile.getName());
			if(newJ != null){
				newJ.jrnlPane.jpages.jcom = jc;
				newJ.jrnlPane.jpages.active = true;
				newJ.jrnlPane.setDisconnect();
				newJ.jrnlPane.setStart();
				newJ.jcom = jcom;
				jcom.setJarnal(newJ);
				jarn.jcom = null;
			}
			else {
				jpages.jcom = jc;
				jpages.active = true;
				setDisconnect();
				setStart();
				jarn.jcom = jcom;
			}
			jcom.start();
			jc.start();
		}
		return;
	    }

	    if(action.equals("Check for Updates"))(new checkForUpdatesListener()).showDialog(gJrnlFrame, jarn);
	    if(action.equals("About")) about();
	    if(action.equals("Documentation")) documentation();
	    if(action.equals("Memory Errors")) aboutmemory();
	    if(action.equals("Hot Keys")) hotkeys();

	    if(action.equals("Show Server Message")) JOptionPane.showConfirmDialog(gJrnlFrame, serverMessage, "Server Message", JOptionPane.DEFAULT_OPTION);

	    if(action.equals("Save On Exit")){
		saveOnExit = !saveOnExit;
		soe.setState(saveOnExit);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Save Options") || action.equals("Save With Options")) (new saveOptionsDialogListener()).showDialog(gJrnlFrame, jarn);

	    if(action.equals("Network Save Options")){
		netSaveDialog(null, null);
		return;
	    }

	    if(action.equals("Save Bookmarks")){
		saveBookmarks = !saveBookmarks;
		sbmk.setState(saveBookmarks);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Print With Options")){
		(new printOptionsListener()).showDialog(gJrnlFrame, jarn);
		action = "fooPrint Options";
	    }

	    if(action.equals("alignToMargins")){
		alignToMargins = !alignToMargins;
		pam.setState(alignToMargins);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("bestFit")){
		bestFit = !bestFit;
		pbf.setState(bestFit);
		//if(bestFit) {
		//	absoluteScale = false;
		//	pas.setState(absoluteScale);
		//}
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("absoluteScale")){
		absoluteScale = !absoluteScale;
		pas.setState(absoluteScale);
		//if(absoluteScale){
		//	bestFit = false;
		//	pbf.setState(bestFit);
		//}
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("showPageNumbers")){
		showPageNumbers = !showPageNumbers;
		psp.setState(showPageNumbers);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Prompt for Net Save Name")){
		promptForNetSaveName = !promptForNetSaveName;
		pfnsn.setState(promptForNetSaveName);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("withBorders")){
		withBorders = !withBorders;
		wbr.setState(withBorders);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Save Self Executing")){
		saveSelfexecuting = !saveSelfexecuting;
		jcbSaveSelfexecuting.setState(saveSelfexecuting);
		if(saveSelfexecuting){
			jpages.portableBgs = true;
			pbgs.setState(jpages.portableBgs);
			saveBg = false;
			action = "Save Background With File";
		}
		dirty = true;
		setSave(false);
	    }

	    if(action.equals("Portable Backgrounds")){
		jpages.portableBgs = !jpages.portableBgs;
		pbgs.setState(jpages.portableBgs);
		if(jpages.portableBgs){
			saveBg = false;
			action = "Save Background With File";
		}
		dirty = true;
		setSave(false);
	    }

	    if(action.equals("Save Background With File")){
		saveBg = !saveBg;
		sbg.setState(saveBg);
		sbg2.setState(saveBg);
		jpages.saveBg = saveBg;
		if(!saveBg){
			jpages.portableBgs = false;
			pbgs.setState(jpages.portableBgs);
		}
		dirty=true;
		setSave(false);
	    }

	    if(action.equals("Autoupdate Bookmarks")){
		updateBookmarks = !updateBookmarks;
		abmk.setState(updateBookmarks);
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Text")){
		if(fullScreen) return;
		textMode = !textMode;
		setCursor();
		dirty = true;
		setStatus("");
		return;
	    }

	    if(action.equals("Print") && printaround) action = "Print via PDF";

	    if(action.equals("Print Background")){
		action = "already Printed Background";
		String src = jpages.getBgsName();
		if(src == null) return;
		if(src.equals("")) return;
		String est = printpdf;
		try{
			est = Jtool.replaceAll(est, "%1", Jtool.cmdQuote(src));
			System.out.println(est);
			Runtime rt = Runtime.getRuntime();
			//rt.exec(est);	
			Process ps = rt.exec(est);
			InputStream is = ps.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
            		BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) System.out.println(line);
			ps.waitFor();
		}
		catch(Exception ex){ex.printStackTrace();}
	    }

	    if(action.equals("Export to PDF")){
		    boolean oalignToMargins = alignToMargins;
    		    boolean obestFit = bestFit;
		    boolean oabsoluteScale = absoluteScale;
    		    boolean oshowPageNumbers = showPageNumbers;
   		    boolean owithBorders = withBorders;
		    alignToMargins = false;
    		    bestFit = false;
		    absoluteScale = true;
    		    showPageNumbers = false;
   		    withBorders = false;
		    String oldcwd = cwd;
		    String temp = getFile("Print as PDF", false);
		    if(temp == null) {	
			cwd = oldcwd;
			return;
		    }
		    pdfFile = (new File(cwd + File.separator + temp)).getPath();
		    cwd = oldcwd;
		    doAction("zPrint as PDF");
		    alignToMargins = oalignToMargins;
    		    bestFit = obestFit;
		    absoluteScale = oabsoluteScale;
    		    showPageNumbers = oshowPageNumbers;
   		    withBorders = owithBorders;
	    }

	    if(action.equals("Print as PDF")){
		String oldcwd = cwd;
		String temp = getFile("Print as PDF", false);
		if(temp == null) {	
			cwd = oldcwd;
			return;
		}
		pdfFile = (new File(cwd + File.separator + temp)).getPath();
		cwd = oldcwd;
	    }

	    if(action.equals("Print via PDF")){
		try{
			pdfFile = File.createTempFile("pspool", ".pdf").getPath();
		}
		catch(Exception ex){ex.printStackTrace();}
	    }

	    if(action.equals("Print")){
		try{
			pj = PrinterJob.getPrinterJob();
			if(!pj.printDialog()) return;
			pj.setPrintable(jrnlPane);
		}
		catch(Exception ex){System.err.println("Printer configuration problem, cannot print"); return;}
	    }

	    if(action.startsWith("Print")){
		cancelPrint = false;
		jtm.doActionWithCancel("xxz" + action, action);
            	return;
	    }

	    if(action.equals("-pdf")){
		pdfFile = pdffile;
		if(pdfFile.equals("-")){
			if(cwd == null) cwd = ".";
			pdfFile = cwd + File.separator + fname;
			pdfFile = pdfFile.substring(0, pdfFile.length() - 3) + "pdf";
		}
		action = "zPrint as PDF";
	    }

	    if(action.equals("zPrint as PDF") || action.equals("zPrint via PDF") || action.equals("zNetSave as PDF")){
		if((pdfFile == null) && !action.equals("zNetSave as PDF")) return;
		JobAttributes ja = new JobAttributes();
		ja.setDestination(JobAttributes.DestinationType.FILE);
		jrnlPDFWriter pdfWriter = null;
		OutputStream fos; 
		if(action.equals("zNetSave as PDF")) fos = netsaveos;
		else{
			try{
				fos = new FileOutputStream(pdfFile);
			}
			catch(Exception ex){System.err.println("Couldn't write " + pdfFile); return;}
		}
		try {
			pdfWriter = new jrnlPDFWriter(fos);
		}
		catch(Error ex){System.err.println("Internal PDF Writer Not Available, Trying External"); pdfWriter = null;}
		try{
			PrintJob pdf = null;
			String tfilename = "";
			if(!isApplet){
				File tfile = File.createTempFile("spool", ".ps");
				tfilename = tfile.getPath();
				ja.setFileName(tfilename);
				ja.setDialog(JobAttributes.DialogType.NONE);
				try{
					pdf = toolkit.getPrintJob(null, "pjob", ja , new PageAttributes());
				}
				catch(Exception ex){System.err.println("Printer configuration problem, continuing."); pdf = null;}
			}
			print(pdf, pdfWriter);
			if(pdf != null) pdf.end();
			if(pdfWriter != null) {
				pdfWriter.writePDF(null, "close", 0, 0);
				fos.close();
			}
			else{
				//jbcancel.msg.setText("Spooling ps to pdf");
				jbcancelmsg("Spooling ps to pdf");
				String est = ps2pdf;
				est = Jtool.replaceAll(est, "%1", Jtool.cmdQuote(tfilename));
				est = Jtool.replaceAll(est, "%2", Jtool.cmdQuote(pdfFile));
				System.out.println(est);
				Runtime rt = Runtime.getRuntime();
				rt.exec(est);	
				Process ps = rt.exec(est);
				InputStream is = ps.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
            			BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) System.out.println(line);
				ps.waitFor();
			}
		}
		catch(Exception ex){ex.printStackTrace();}

		if(action.equals("zPrint via PDF")){
			String est = printpdf;
			try{
				est = Jtool.replaceAll(est, "%1", pdfFile);
				System.out.println(est);
				Runtime rt = Runtime.getRuntime();
				Process ps = rt.exec(est);
				InputStream is = ps.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
            			BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) System.out.println(line);
				ps.waitFor();
			}
			catch(Exception ex){ex.printStackTrace();}
		}		
	    }
		
	    if(action.equals("zPrint")){
		try {
			pj.print();
		}
		catch(PrinterException pe){
			System.out.println(pe.toString());
		}
	    	return;
	    }

	    if(action.equals("Restore Default Toolbars")){
		setToolbars(null, null);
		tb3 = dtb3;
		return;
	    }
	    if(action.equals("Load Top Toolbar")){
		loadToolbar(1);
		return;
	    }

	    if(action.equals("Load Bottom Toolbar")){
		loadToolbar(2);
		return;
	    }

	    if(action.equals("Load Presentation Toolbar")){
		loadToolbar(3);
		return;
	    }

	    if(action.equals("Escape")) {
		fromFullScreen();
		fullScreen = false;
	    }

	    if(action.equals("Full Screen")){
		if(fullScreen) fromFullScreen();
		else {
			activePage = 0;
			offX = foffX;
			offY = foffY;
			if(thumbs && !threeup) jpages.setScale(2 * jpages.getScale());
			thumbs = false;
			threeup =false;
			textMode = false;
			jpages.invalidateGraphics();
			toFullScreen();
		}
		fullScreen = !fullScreen;
		if(fullScreen) resize();
	    }
	
	    boolean onBackground = false;

	    if(action.equals("New On Background")){
		String oldcwd = cwd;
		if(bwd != null) cwd = bwd;
		String temp = getFile("Open New On Background", false);
		bwd = cwd;
		cwd = oldcwd;
		if(temp == null) return;
		bgfile = bwd + File.separator + temp;
		action = "New";
		onBackground = true;
	    }

	    if(action.equals("New")) {
		jpages.invalidateGraphics();
		Jarnal jrnl = Jarnal.newJarnal("Jarnal");
		jrnl.jrnlPane.setConf(getConf());
		jrnl.jrnlPane.setToolbars(tb1, tb2);
		String spp = jpages.getDefaultPaper();
		Jpaper pp = new Jpaper();
		pp.setConf(spp);
		pp.bgid = "none";
		jrnl.jrnlPane.jpages.setPaperConf(pp.getConf());
		jrnl.nname = "unsaved.jaj";
		jrnl.saveBookmarks = false;
		jrnl.sbmk.setState(false);
		jrnl.cwd = cwd;
		jrnl.iwd = iwd;
		jrnl.bwd = bwd;
		if(onBackground){
			jrnl.jrnlPane.jpages.openBg(new JbgsSource(bgfile,null));;
			jrnl.fitWidth = true;
			jrnl.jrnlPane.resize();
			jrnl.dirty = true;
		}
		//else{
		jrnl.jrnlPane.setCursor();
		jrnl.jrnlPane.setStatus("");
		//}
		return;
	    }

	    if(action.indexOf("Save") > -1) jtm.setClockCursor(null);

	    boolean thenExit = false;
	    if(action.equals("Network Save and Exit")){
		action = "Network Save";
		thenExit = true;
		dirty = false;
	    }
	    if(action.equals("Save and Exit")){
		action = "Save";
		thenExit = true;
	    }

	    if(action.equals("Network Save")){
		if(promptForNetSaveName && nname.equals("unsaved.jaj")) getNetSaveName();
		Hashtable ht = new Hashtable();
		String tfname = fname;
		if(tfname.equals("")) tfname="unsaved.jaj";
		ht.put("$f", tfname);
		ht.put("$p", cwd + File.separator + tfname);
		ht.put("$n", "" + jpages.getPages());
		if(uniqueID == null) uniqueID = suggestName() + Long.toString((new Random()).nextLong(), 36);
		ht.put("$u", uniqueID);
		ht.put("$g", nname);
		HtmlPost hp = new HtmlPost(netServer, netOptions, jpages, ht, getConf(), urlencoded);
		hp.setJarnal(jarn);
		hp.withBorders = withBorders;
		hp.post();
		serverMessage = hp.serverMsg;
		if(isNetSave && !hp.netError){
			dirty=false;
			setStatus("");
		}
		if(thenExit && !hp.netError) action = "Exit";
		else {
			if(!hp.netError) return;
			else {
				if(!isApplet){
					action = "Save";
					JOptionPane.showConfirmDialog(gJrnlFrame, "Network connection failed, will attempt local save", "Warning", JOptionPane.DEFAULT_OPTION);
				}
				else {
					JOptionPane.showConfirmDialog(gJrnlFrame, "Network connection failed, cannot save locally", "Warning", JOptionPane.DEFAULT_OPTION);
					if(thenExit) action = "Exit";
				}
			}
		}
	    }

	    if(action.equals("Savex")){
		if(fname.equals("")){
			String temp = getFile("Save", true);
			if(temp == null) return;
			if((new File(cwd + File.separator + temp)).exists()){
				int n = JOptionPane.showConfirmDialog(gJrnlFrame, cwd + File.separator + temp + " already exists. Ok to overwrite?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.NO_OPTION) return;
			}
			fname = temp;
			addBookmarkAll(cwd + File.separator + fname);
		}
		action = "Save";
	    }

	    boolean noDialog = false;
	    if(action.equals("Save No Dialog")){
		action = "Save";
		noDialog = true;
	    }
            
	    if(action.equals("Save")){
		if(fname.equals("") && noDialog) return;
		if(fname.equals("")){
			//doAction("Save With Options");
			saveOptionsDialogListener fooster = new saveOptionsDialogListener();
			fooster.showDialog(gJrnlFrame, jarn);
			if(thenExit) fooster.setExit();
			return;
		}
		if(cwd == null) cwd = ".";
		boolean isSaved = jpages.save(cwd + File.separator + fname, getConf());
		if(!embed) gJrnlFrame.setTitle("Jarnal - " + fname); 
		if(!isNetSave)dirty = !isSaved;
		if(isSaved && saveSelfexecuting) Selfexec.pack("jarnal.jar", cwd, fname);
		setSave(false); 
		if(thenExit) action = "Exit";
		else return;
	    }

            if(action.equals("Save As")){
		String temp = getFile("Save As", true);
		if(temp!=null){
			if((new File(cwd + File.separator + temp)).exists()){
				int n = JOptionPane.showConfirmDialog(gJrnlFrame, cwd + File.separator + temp + " already exists. Ok to overwrite?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.NO_OPTION) return;
			}
			uniqueID = suggestName() + Long.toString((new Random()).nextLong(), 36);
			fname = temp;
			addBookmarkAll(cwd + File.separator + fname);
			dirty = !jpages.save(cwd + File.separator + fname, getConf()); 
			if(!dirty) internalName = "";
			if(!embed) gJrnlFrame.setTitle("Jarnal - " + fname); 
			if(saveSelfexecuting) Selfexec.pack("jarnal.jar", cwd, fname);
			setSave(false);
		}
		return;
	    }

	    if(action.equals("Send As Email")){
		String ans = (new Jarnbox(gJrnlFrame, "Email To", jarn, true)).getString(email);
		if(ans == null) return;
		email = ans;
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		jpages().save(bas, getConf());
		byte ba[] = bas.toByteArray();
		byte bav[][] = new byte[1][];
		bav[0] = ba;
		Jsendmail jsm = new Jsendmail();
		String sendname = fname;
		if(sendname.equals("")) sendname = "temp.jaj";
		String sn[] = new String[1];
		sn[0] = sendname;
		String an[] = new String[1];
		an[0] = "jarnal-jaj";
		ans = jsm.sendmail(null, null, ans, "Jarnal file attached", "See attached Jarnal file", sn, an, bav);
		if(ans != null)	JOptionPane.showConfirmDialog(gJrnlFrame, ans, "Mail Error",  JOptionPane.DEFAULT_OPTION);
	    }

	    if(action.equals("Send PDF as Email")){
		String ans = (new Jarnbox(gJrnlFrame, "Email PDF To", jarn, true)).getString(email);
		if(ans == null) return;
		email = ans;
		byte ba[];
		try{
			File tfile = File.createTempFile("temppdf", "pdf");
			pdfFile = tfile.getPath();
			doAction("zPrint as PDF");
			ba = jpages.streamToByteArray(new FileInputStream(tfile));
		}
		catch(Exception ex){ex.printStackTrace(); return;}
		byte bav[][] = new byte[1][];
		bav[0] = ba;
		Jsendmail jsm = new Jsendmail();
		String sendname = fname;
		if(sendname.equals("")) sendname = "temp.pdf";
		else sendname = fname + ".pdf";
		String sn[] = new String[1];
		sn[0] = sendname;
		String an[] = new String[1];
		an[0] = "pdf";
		ans = jsm.sendmail(null, null, ans, "PDF file attached", "See attached PDF file", sn, an, bav);
		if(ans != null)	JOptionPane.showConfirmDialog(gJrnlFrame, ans, "Mail Error",  JOptionPane.DEFAULT_OPTION);	
	    }

	    if(action.equals("Save Text")){
		if(textfile == null) action = "Save Text As";
		else action = "save text";
	    }
	    if(action.equals("Save Text As")){
		String temp = getFile("Save Text", false);
		if(temp==null) return;
		textfile = temp;
		action = "save text";
	   }
	   String alltext = "";
	   if(action.equals("Save Html As")){
		String temp = getFile("Save Html", false);
		if(temp==null) return;
		textfile = temp;
		action = "save all text";
		alltext = jpages.copyAllHtml();
	   }
	   if(action.equals("save text")){
		alltext = jpages.copyAllText();
		action = "save all text";
	   }
	   if(action.equals("save all text")){		
		try{
			FileOutputStream out = new FileOutputStream(cwd + File.separator + textfile);
			out.write(alltext.getBytes());
			out.close();
		}
		catch(Exception ex){System.err.println("file write error writing textfile " + textfile + "\n" + ex);}
		dirty = true; 
		setStatus("");
		return;
	    }

	    if(action.equals("Network Save Name")){
		getNetSaveName();
	    }

	    if(action.equals("Snapshot")){
		String oldcwd = cwd;
		String temp = getFile("Save Snapshot", false);
		if(temp!=null){
			writeGraphicFile(new File(cwd + File.separator + temp), "jpg");	
		}
		cwd = oldcwd;
	    }

	    if(action.equals("Export to TIFF")){
		String oldcwd = cwd;
		String temp = getFile("Save TIFF", false);
		if(temp != null){
			writeTIFFGraphicFile(new File(cwd + File.separator + temp));
		}
		cwd = oldcwd;
	    }

	    if(action.equals("Open")){
		String oldcwd = cwd;
		String temp = getFile("Open", true);
		String newcwd = cwd;
		cwd = oldcwd;
		openName(newcwd, temp);
	    }

	    if(action.equals("Edit Current Template")){
		if(templateFile == null) {
			JOptionPane.showConfirmDialog(gJrnlFrame, "No template currently open", "Open Error",  JOptionPane.DEFAULT_OPTION);
			return;
		}
		File ftemp = new File(templateFile);
		openName(ftemp.getParent(), ftemp.getName());
	    }

	    if(action.equals("OpenURL")){
		String ans = (new Jarnbox(gJrnlFrame, "OpenURL", jarn, true)).getString("");
		if(ans == null) return;
			try{
				HtmlPost hp = new HtmlPost(ans, null, null, null, null, false);
				String newfilen = hp.pipe(".jaj");
				File newfile = new File(newfilen);
				openName(newfile.getParent(), newfile.getName());
			}
			catch(Exception ex){System.err.println("Can't open URL: " + ans);}			
	    }

	    if(action.equals("Exit")){
		if(winDone()){
			if(!embed) gJrnlFrame.setVisible(false);
			checkClose();
		}
		return;
	    }

	    if(action.equals("Shift Right"))
		(new Jarnbox(gJrnlFrame, "Shift Right", jarn, false)).getInt(foffX, -1000, 1000, true);

	    if(action.equals("Shift Down"))
		(new Jarnbox(gJrnlFrame, "Shift Down", jarn, false)).getInt(foffY, -1000, 1000, true);

	    if(action.indexOf("Zoom") > -1) jtm.setClockCursor(null);
	    
	    if(action.equals("Zoom")){
		(new Jarnbox(gJrnlFrame, "Zoom", jarn, false)).getPosDecNumber(jpages.getScale(),100.0f, true);
	    }
	    if(action.equals("Preview Zoom")){
		if(previewZoom != null) {
			float scale = previewZoom.floatValue();
			float oldscale = jpages.getScale();
			if(scale != oldscale){
				jpages.invalidateGraphics();
				jpages.setScale(scale);
				zoom(oldscale);
			}
		}
	    }
	    if(action.equals("Zoom In")){
		fitWidth = false;
		jpages.invalidateGraphics();
		float oldscale = jpages.getScale();
		jpages.upScale(1);
		zoom(oldscale);
	    }
            if(action.equals("Zoom Out")){
		fitWidth = false;
		jpages.invalidateGraphics();
		float oldscale = jpages.getScale();
		jpages.upScale(-1);
		zoom(oldscale);
	    }
	    if(action.equals("Fit Width")){
		fitWidth = true;
		resize();
	    }

	    if(action.equals("Background Information")){
		JOptionPane.showConfirmDialog(gJrnlFrame, jpages.bgs().getInfo(), "Background File Information", JOptionPane.DEFAULT_OPTION);
	    }

	    if(action.equals("Playback")){
		if(!replayActive){
			locked = true;
			dragOp = 100;
			replayActive = true;
			(new Jarnbox(gJrnlFrame, "Playback", jarn, false)).showReplay();
		}
	    }

	    if(action.equals("mini Jarnal")){
		miniJarnal("Recognize Text");
	    }

	    if(action.equals("Outline")){
		Container cp = gJrnlFrame.getContentPane();
		if((tpanel != null) && !showOutline){
			cp.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
		}
		if(tpanel == null){
			showOutline = true;
			try{
				JPanel jpa = new JPanel(new BorderLayout());
				outline.initOut(jpa);
				outline.synchPage(1);
				tpanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpa, sp);
				tpanel.setDividerSize(1);
				tpanel.setDividerLocation(outheight);
				cp.add(tpanel, BorderLayout.CENTER);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else{
			cp.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
			showOutline = false;
		}
		Dimension d = new Dimension(gJrnlFrame.getSize().width + 1, gJrnlFrame.getSize().height + 1);
		gJrnlFrame.setSize(d);
	     }

	    if(action.equals("Update Thumbnail Bar")){
		if(tpanel == null) return;

		try{
			File tfile = File.createTempFile("bar", ".jaj");
			tfile.deleteOnExit();
			jpages.save(tfile.getPath(), getConf());		
			JPanel miniFrame = new JPanel(new BorderLayout());		
			Jarnal mj = barJarnal(tfile.getPath(), jarn, miniFrame, toolkit);
			mj.jrnlPane.tpanel = tpanel;
			tpanel.setLeftComponent(mj.sp);
			tpanel.setRightComponent(sp);
			mj.jrnlPane.doAction("Fit Width");
		}
		catch(Exception ex){ex.printStackTrace();}

		Dimension d = new Dimension(gJrnlFrame.getSize().width + 1, gJrnlFrame.getSize().height + 1);
		gJrnlFrame.setSize(d);
	    }
		

	    if(action.equals("Thumbnail Bar")){
		Container cp = gJrnlFrame.getContentPane();	
		if((tpanel != null) && showOutline){
			cp.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
		}
		showOutline = false;
		if(tpanel == null){
			try{
				File tfile = File.createTempFile("bar", ".jaj");
				tfile.deleteOnExit();
				jpages.save(tfile.getPath(), getConf());		
				JPanel miniFrame = new JPanel(new BorderLayout());		
				Jarnal mj = barJarnal(tfile.getPath(), jarn, miniFrame, toolkit);
				tpanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mj.sp, sp);
				//tpanel.setOneTouchExpandable(true);
				tpanel.setDividerLocation(divwidth);
				cp.add(tpanel, BorderLayout.CENTER);
				mj.jrnlPane.tpanel = tpanel;
				mj.jrnlPane.doAction("Fit Width");
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else{
			cp.remove(tpanel);
			tpanel = null;
			cp.add(sp, BorderLayout.CENTER);
		}
		Dimension d = new Dimension(gJrnlFrame.getSize().width + 1, gJrnlFrame.getSize().height + 1);
		gJrnlFrame.setSize(d);	
	    }

	    if(action.equals("Internal mini Jarnal")){
		if(barjarnal) return;
		Container cp = gJrnlFrame.getContentPane();
		//if(tpanel != null){
		//	cp.remove(tpanel);
		//	tpanel = null;
		//	cp.add(sp, BorderLayout.CENTER);
		//}		
		if(bpanel == null){			
			JPanel miniFrame = new JPanel(new BorderLayout());		
			Jarnal mj = miniJarnal(null, miniFrame, toolkit);
			mj.jrnlPane.jpages.setWidth(8.5f);
			mj.parentJarn = jarn;
			bpanel = new JPanel(new BorderLayout());
			cp.remove(statusBar);
			bpanel.add(miniFrame, BorderLayout.CENTER);
			bpanel.add(statusBar, BorderLayout.SOUTH);
			cp.add(bpanel, BorderLayout.SOUTH);
		}
		else{
			cp.remove(bpanel);
			bpanel = null;
			cp.add(statusBar, BorderLayout.SOUTH);
		}
		Dimension d = new Dimension(gJrnlFrame.getSize().width + 1, gJrnlFrame.getSize().height + 1);
		gJrnlFrame.setSize(d);		
	    }

	    if(action.equals("Find")){
		(new Jarnbox(gJrnlFrame, "Find", jarn, false)).showFind();
	    }		

	    //dragOp = 100 locks the GUI
	    //operations that change the view should go before this statement
	    //operations that are undoable should go after

	    middleIgnore = zmiddleIgnore;

	    if(dragOp == 100){
	        jrnlPane.repaint();
            	jrnlPane.requestFocus();
	    	setStatus("");
		return;
	    }

	    if(action.equals("Next Frame")) {
		advanceFrame();
		jpages.nextFrame();
		dirty = true;
	    }
	    if(action.equals("Previous Frame")) previousFrame();

	    if(action.equals("Restore Default Configuration")){
		setDefaultConf();
		dirty = true;
		setCursor();
		setStatus("");
	    }

	   if(action.equals("Insert Screenshot")) (new screenShotListener()).showDialog(gJrnlFrame, jarn, "Insert Screenshot");
	   if(action.equals("Background Screenshot")) (new screenShotListener()).showDialog(gJrnlFrame, jarn, "Background Screenshot");

	   if(action.equals("Screenshot Background")){
		if(mscr) gJrnlFrame.setState(Frame.ICONIFIED);
                try { Thread.sleep(1000); }
		catch (InterruptedException ex) { ex.printStackTrace(); }
		Jarnscr jscr = new Jarnscr(this, false, ascr);
		(new Thread(jscr)).start();
	   }

	   if(action.equals("Screenshot Image")){
		if(mscr) gJrnlFrame.setState(Frame.ICONIFIED);
                try { Thread.sleep(1000); }
		catch (InterruptedException ex) { ex.printStackTrace(); }
		Jarnscr jscr = new Jarnscr(this, true, ascr);
		(new Thread(jscr)).start();
	   }

	   if(action.equals("Background for Insertion")){
		if(mscr) gJrnlFrame.setState(Frame.NORMAL);
		if(scr != null){
			try{
				File tfile = File.createTempFile("ScreenCapture" + (new Random()).nextInt(), ".png");
				String bgfile = tfile.getPath();
				javax.imageio.ImageIO.write(scr, "PNG", tfile);
				jpages.openBg(new JbgsSource(bgfile,null));
			}
			catch(Exception ex){System.err.println(ex); scr = null;}
			if(scr == null) return;			
			fitWidth = true;
			resize();
			dirty = true;
			setup();
		}
		scr = null;
	   }

	   if(action.equals("Image for Insertion")){
		if(mscr) gJrnlFrame.setState(Frame.NORMAL);
		if(scr != null){
			ByteArrayOutputStream baost = new ByteArrayOutputStream();
			try{
				javax.imageio.ImageIO.write(scr, "PNG", baost);
			}
			catch(Exception ex){System.err.println(ex); baost = null; scr = null;}
			if(baost == null) return;
			ByteArrayInputStream baist = new ByteArrayInputStream(baost.toByteArray());
			nextScrap = jpages.addScrapImage(baist, "ScreenCapture" + (new Random()).nextInt() + ".png");
			scr = null;
			dragOp = 113;
			standardCursor();
			setStatus("");
			return;
		}
		scr = null;
	   }

	   if(action.equals("Minimize for Screenshot")){
		mscr = !mscr;
		scr1.setState(mscr);
		scr2.setState(mscr);
		scr3.setState(mscr);
		return;
	   }

	   if(action.equals("Entire Screen")){
		ascr = !ascr;
		ascr1.setState(ascr);
		ascr2.setState(ascr);
		ascr3.setState(ascr);
		return;
	   }

	   if(action.startsWith("Insert Jarnal")){
		String temp = getFile("Insert Jarnal", false);
		if(temp == null) return;
		String insfile = cwd + File.separator + temp;
		//jpages.insert(insfile, textMode);
		jpages.insert(insfile, action);
		setup();
		setStatus("");
	    }

	   if(action.equals("Insert Link")){
		dragOp = 117;
		standardCursor();
		setStatus("");
	   }

	   if(action.equals("Insert Image")){
		String oldcwd = cwd;
		if(iwd != null) cwd = iwd;
		String temp = getFile("Insert Image", false);
		iwd = cwd;
		cwd = oldcwd;
		String oldScrap = nextScrap;
		if(temp == null) return;
		String scrapfile = iwd + File.separator + temp;
		try{
			nextScrap = jpages.addScrapImage(new FileInputStream(scrapfile), temp);
		}
		catch(Exception ex){System.err.println(ex); nextScrap = oldScrap;}
		dragOp = 113;
		standardCursor();
		setStatus("");
		return;
	    }
		
	   if(action.equals("Repeating")){
		jpages.setRepeating(!jpages.getRepeating());
		rbh.setState(jpages.getRepeating());
		dirty = true;
		setup();
	   }

	   if(action.equals("Insert Background Text")){
		if(!insBgTextPages()) return;
		dirty = true;
	   }
	   else if(action.startsWith("Insert Background")){
		String oldcwd = cwd;
		if(bwd != null) cwd = bwd;
		String temp = getFile("Insert Background", false);
		bwd = cwd;
		cwd = oldcwd;
		if(temp == null) return;
		bgfile = bwd + File.separator + temp;
		jpages.insertBg(new JbgsSource(bgfile,null), action);
		fitWidth = true;
		resize();
		dirty = true;
		setup();
	    }
	   if(action.equals("Open Background")){
		String oldcwd = cwd;
		if(bwd != null) cwd = bwd;
		String temp = getFile("Open Background", false);
		bwd = cwd;
		cwd = oldcwd;
		if(temp == null) return;
		bgfile = bwd + File.separator + temp;
		jpages.openBg(new JbgsSource(bgfile,null));;
		fitWidth = true;
		resize();
		dirty = true;
		setup();
	    }

	    if(action.equals("Open Text")){
		String oldcwd = cwd;
		String temp = getFile("Open Text", false);
		if(temp == null) return;
		textfile = temp;
		String instr = null;
		opentext();
		setup();
	    }
		

	    if(action.equals("Remove Background")){
		bgfile = "";
		jpages.removeBg();
		dirty = true;
		setup();
	    }

	    if(action.equals("Text Style")) (new textDialogListener()).showDialog(gJrnlFrame, jarn);

	    if(action.equals("Black Text")) {textOp("style", 0, "Colorblack"); return;}
	    if(action.equals("Blue Text")) {textOp("style", 0, "Colorblue"); return;}
	    if(action.equals("Gray Text")) {textOp("style", 0, "Colordark gray"); return;}
	    if(action.equals("Green Text")) {textOp("style", 0, "Colorgreen"); return;}
	    if(action.equals("Magenta Text")) {textOp("style", 0, "Colormagenta"); return;}
	    if(action.equals("Orange Text")) {textOp("style", 0, "Colororange"); return;}
	    if(action.equals("Pink Text")) {textOp("style", 0, "Colorpink"); return;}
	    if(action.equals("Red Text")) {textOp("style", 0, "Colorred"); return;}
	    if(action.equals("White Text")) {textOp("style", 0, "Colorwhite"); return;}
	    if(action.equals("Yellow Text")) {textOp("style", 0, "Coloryellow"); return;}
	    if(action.equals("Bold Text")) {textOp("style", 0, "Bold"); return;}
	    if(action.equals("Italic Text")) {textOp("style", 0, "Italic"); return;}
	    if(action.equals("Underline Text")) {textOp("style", 0, "Underline"); return;}

	    if(action.equals("Set Text Default")) jpages.setDefaultParms();
	    if(action.equals("Set Default Paper")) jpages.setDefaultPaper();
	    if(action.equals("Apply Paper to All Pages") || action.equals("Apply to All with Background")){
		Jpaper jpap = jpages.setDefaultPaper();
		String bghandle = jpap.bgid;
		boolean samebg = false;
		if(action.equals("Apply to All with Background")) samebg = true;
		int savePage = jpages.getPage();
		jpages.nextPage(-savePage);
		for(int ii = 0; ii < jpages.getPages(); ii++){
			if(!samebg) jpages.setPaper(jpap, false);
			else if(jpages.getPaper().bgid.equals(bghandle)) jpages.setPaper(jpap, false);
			jpages.nextPage(1);
		}
		jpages.nextPage(savePage - jpages.getPage());
		setup();
		dirty = true;
		jpages.invalidateGraphics();
		drawState = 0;
	    }

	   if(action.equals("Properties")){
		jpages.selectSingle(popupPoint, jt);
		if(jpages.overlaySelected()) action = "Overlay Style";
		else action = "Modify Selection";
	   }

	   if(action.equals("Overlay Style")) (new overlayDialogListener()).showDialog(gJrnlFrame, jarn);

	   boolean overlayOp = false;
	   if(action.endsWith(" overlay")){
		overlayOp = true;
		int ncol = action.indexOf(" overlay");
		if(ncol > 0) {
			String ocol = action.substring(0, ncol);
			defaultOverlay = jpages.setOverlayColor(defaultOverlay, ocol);
		}
	    }
	    if(action.equals("Refresh Overlay")){
		overlayOp = true;
		Number test = (new Jarnbox(gJrnlFrame, "Refresh Overlay")).getInt(jpages.getOverlayFade(defaultOverlay), 100);
		if(test != null) defaultOverlay = jpages.setOverlayStyle(defaultOverlay, -1, -1, null, null, -1, test.intValue(), -1);
	    }
	    if(action.equals("Overlay Outline Thickness")){
		overlayOp = true;
		Number test = (new Jarnbox(gJrnlFrame, "Overlay Outline Thickness")).getInt(jpages.getOutlineThickeness(defaultOverlay), 20);
		if(test != null) defaultOverlay = jpages.setOverlayStyle(defaultOverlay, -1, -1, null, null, test.intValue(), -1, -1);
	    }
	    if(action.endsWith(" outline")){
		overlayOp = true;
		int ncol = action.indexOf(" outline");
		if(ncol > 0) {
			String ocol = action.substring(0, ncol);
			defaultOverlay = jpages.setOverlayOutline(defaultOverlay, ocol);
		}
	    }
	    if(overlayOp){
		dirty = true;
		jpages.invalidateGraphics();
		drawState = 0;
	    }		

	    if(action.length() >= 7){
		if(action.substring(0,6).equals("file:/")){
			String temps = action.substring(6);
			File temp = new File(temps);
			openName(temp.getParent(), temp.getName());
			return;
		}
	    }

	    if(action.length() >= 9){
		if(action.substring(0,8).equals("delete:/")){
			String temps = action.substring(8);
			bookmarks.remove(temps);
			setMarks(getBookmarks(), BOOKMARKS);
			dirty = true;
			setStatus("");
			return;
		}
	    }

	    if(action.length() >= 16){
		if(action.substring(0,15).equals("delete server:/")){
			String temps = action.substring(15);
			servermarks.remove(temps);
			setMarks(getServermarks(), SERVERS);
			dirty = true;
			setStatus("");
			return;
		}
	    }

	    if(action.length() == 4){
		if(action.substring(2,4).equals("pt")){
			textOp("style", Integer.parseInt(action.substring(0,2).trim()), "Size");
		}
	    }

	    if(action.length() >= 5){
		if(action.substring(0,5).equals("Font ")){
			textOp("style", 0, action);
		}
	    }

	    if(action.equals("Stamp Date")) {textOp("type", 0, getDate()); return;}

	    if(action.equals("Modify Selection")) (new selectionDialogListener()).showDialog(gJrnlFrame, jt, jarn);

	    if(action.equals("Arrow Weight")){
		jpages.applyArrow(markerweight);
		dirty = true;
	    }

	    if(action.equals("Paper and Background")) (new paperDialogListener()).showDialog(gJrnlFrame, jarn);

	    if(action.equals("white paper")){jpages.setPaper(Jpaper.WHITE); dirty = true;}
	    if(action.equals("yellow paper")){jpages.setPaper(Jpaper.lighter(Color.yellow.getRGB())); dirty = true;}
	    if(action.equals("pink paper")){jpages.setPaper(Jpaper.lighter(Color.pink.getRGB())); dirty = true;}
	    if(action.equals("orange paper")){jpages.setPaper(0xffcc99); dirty = true;}
	    if(action.equals("blue paper")){jpages.setPaper(0xccffff); dirty = true;}
	    if(action.equals("green paper")){jpages.setPaper(0x99ff99); dirty = true;}
		
	    if(action.equals("Lined")) {jpages.setPaper("Lined"); dirty = true;}
	    if(action.equals("Plain")) {jpages.setPaper("Plain"); dirty = true;}
	    if(action.equals("Graph")) {jpages.setPaper("Graph"); dirty = true;}
	    if(action.equals("Graph Paper")){
		String paper = jpages.getPaper().paper;
		if(paper.equals("Graph")) jpages.setPaper("Plain");
		else jpages.setPaper("Graph");
		dirty = true;
	    }
	    if(action.equals("Ruled")) {jpages.setPaper("Ruled"); dirty = true;}
	    if(action.equals("Thick Lines")) {jpages.setLines(15); dirty = true;}
	    if(action.equals("Medium Lines")) {jpages.setLines(25); dirty = true;}
	    if(action.equals("Thin Lines")) {jpages.setLines(35); dirty = true;}
	    if(action.equals("Other Lines")){
		Number test = (new Jarnbox(gJrnlFrame, "Other Lines")).getInt(25, 1000);
		if(test != null) {
			jpages.setLines(test.intValue());
			drawState = 0;
			dirty = true;
		}
	    }

	    boolean resize = false;

	    if(action.equals("Fit Page Width")) {
		jpages.setBgToPaper(true);
		resize = true;
		dirty = true;
	    }
	    if(action.equals("Fit Page Height")) {
		jpages.setBgToPaper(false);
		resize = true;
		dirty = true;
	    }

	    if(action.equals("Fade Background")){
		Number test = (new Jarnbox(gJrnlFrame, "Fade Background")).getInt(jpages.bgFade(), 100);
		if(test != null) {
			jpages.bgFade(test.intValue());
			resize = true;
			dirty = true;
		}
	    }
	    if(action.equals("Rotate Background")){
		jpages.setStartMark();
		jpages.bgRotate(1);
		jpages.setSizeToBg();
		jpages.setEndMark();
		resize = true;
		dirty = true;
	    }

	    if(action.equals("Background Scale")){
		Number test = (new Jarnbox(gJrnlFrame, "Background Scale")).getPosDecNumber(jpages.bgScale(), 100);
		if(test != null) {
			jpages.bgScale(test.floatValue());
			resize = true;
			dirty = true;
		}
	    }
	    if(action.equals("Background Page")){
		Number test = (new Jarnbox(gJrnlFrame, "Background Page")).getInt(jpages.bgindex(), 1000);
		if(test != null) {
			jpages.bgindex(test.intValue());
			resize = true;
			dirty = true;
		}
	    }
	    if(action.equals("Toggle Landscape")) {jpages.toggleLandscape(); resize = true; dirty = true;}
	    if(action.equals("Show Background")) {shbg.setState(jpages.toggleBackground()); resize = true; dirty = true;}
	    if(action.equals("translucent")){jpages.setTransparency(100); resize = true; dirty = true;}
	    if(action.equals("transparent")){jpages.setTransparency(60); resize = true; dirty = true;}
	    if(action.equals("opaque")){jpages.setTransparency(255); resize = true; dirty = true;}
	    if(action.equals("other transparency")){
		Number ans = (new Jarnbox(gJrnlFrame, "Transparency")).getInt(255,255);
		drawState = 0;
		if(ans != null) {
			int test = ans.intValue();
			jpages.setTransparency(test);
			resize = true;
			dirty = true;
		}
	    }


	    if(action.equals("Index Card")) {
		jpages.setStartMark();
		jpages.setHeight(3.0f);
		jpages.setWidth(5.0f);
		jpages.setLines(40);
		jpages.setPaper("Ruled");
		jpages.setEndMark();
		resize = true;
		dirty = true;
	    }
	    if(action.equals("Normal Size")) {
		jpages.setStartMark();
		jpages.setHeight(10.25f);
		jpages.setWidth(8.5f);
		jpages.setLines(25);
		jpages.setEndMark();
		resize = true;
		dirty = true;
	    }
	    if(action.equals("A4")) {
		jpages.setStartMark();
		jpages.setHeight(11.69f);
		jpages.setWidth(8.27f);
		jpages.setLines(25);
		jpages.setEndMark();
		resize = true;
		dirty = true;
	    }
	    if(action.equals("US Letter")) {
		jpages.setStartMark();
		jpages.setHeight(10.98f);
		jpages.setWidth(8.50f);
		jpages.setLines(25);
		jpages.setEndMark();
		resize = true;
		dirty = true;
	    }
	    if(action.equals("Fit to Background")){
		jpages.setSizeToBg();
		resize = true;
		dirty = true;
	    }
	    if(resize){
		setup();
	    }

	    if(action.equals("Fat Width")){
		Number test = (new Jarnbox(gJrnlFrame, "Fat Width")).getPosDecNumber(jth.fatWidth, 100.00);
		if(test != null) {
			jth.fatWidth = test.floatValue();
			jth.setWidth(jth.type);
			jtd.fatWidth = test.floatValue();
			jtd.setWidth(jtd.type);
			jtbu.fatWidth = test.floatValue();
			jtbu.setWidth(jtbu.type);
			jt.fatWidth = test.floatValue();
			jt.setWidth(jt.type);
			jpages.jtool.fatWidth = test.floatValue();
			jpages.jtool.setWidth(jpages.jtool.type);
			dirty = true;
		}
	    }		

	    if(action.equals("Base Pen Thickness")){
		Number test = (new Jarnbox(gJrnlFrame, "Base Pen Thickness")).getPosDecNumber(jt.bWidth, 100.00);
		if(test != null) {
			jth.bWidth = test.floatValue();
			jtd.bWidth = jth.bWidth;
			jtbu.bWidth = jth.bWidth;
			jt.bWidth = jth.bWidth;
			jpages.jtool.bWidth = jth.bWidth;
		}
		dirty = true;
	    }

	    if(action.equals("Base Highlighter Transparency")){
		Number test = (new Jarnbox(gJrnlFrame, "Base Highlighter Transparency")).getPosDecNumber(jt.hTrans, 100.00);
		if(test != null) {
			jth.hTrans = test.floatValue();
			jtd.hTrans = jth.hTrans;
			jtbu.hTrans = jth.hTrans;
			jt.hTrans = jth.hTrans;
			jpages.jtool.hTrans = jth.hTrans;
		}
		dirty = true;
	    }

	    if(action.equals("Page Overlay")){
		Number test = (new Jarnbox(gJrnlFrame, "Page Overlay")).getPosDecNumber(jpages.PO, 100.00);
		if(test != null) {
			jpages.PO = test.floatValue();
		}
		dirty = true;
	    }


	    int bq = viewQuality % 16;
	    int fq = 16 * (viewQuality / 16);
	    if(action.equals("Default Quality")) {bq = 0;}
	    if(action.equals("Good Quality")) {bq = 1;}
	    if(action.equals("Highest Quality")) {bq = 2;}
	    if(action.equals("Normal Quality")) {fq = 0x40;}
	    if(action.equals("High Quality")) {fq = 0x80;}
	    if(action.equals("Low Quality")) {fq = 0;}
	    if(viewQuality != bq + fq){
		viewQuality = bq + fq;
		dirty = true;
		jpages.invalidateGraphics();
		drawState = 0;
		setVQ();
	    }

	    if(action.equals("Silent External Renderer")){
		Jbgs.silentGS = !Jbgs.silentGS;
		backSilentGS.setState(Jbgs.silentGS);
		if(Jbgs.silentGS){
			Jbgs.useGS = true;
			backUseGS.setState(Jbgs.useGS);
		}
		jpages.invalidateGraphics();
		drawState = 0;
	    }

	    if(action.equals("Use External Renderer")){
		Jbgs.useGS = !Jbgs.useGS;
		backUseGS.setState(Jbgs.useGS);
		if(!Jbgs.useGS){
			Jbgs.silentGS = false;
			backSilentGS.setState(Jbgs.silentGS);
		}
		jpages.invalidateGraphics();
		drawState = 0;
	    }

	    if(action.equals("Draw Arrow")){
		arrowhead = true;
		temparrow = true;
		arcb.setState(false);
		action = "Ruler";
	    }		

	    if(action.equals("Arrow")){
		arrowhead = !arrowhead;
		temparrow = false;
		arcb.setState(arrowhead);
		action = "Ruler";
	    }

	    //this is what the different dragOps are
	    //dragOp = 0   in text mode rescaling a text box or image
	    //             not in text mode this is the usual drawing operation default
	    if(action.equals("Razor")) {textMode = false; dragOp = 1; dirty = true; standardCursor();}
	    if(action.equals("Select")) {textMode = false; dragOp = 2; dirty = true; standardCursor();}
	    if(action.equals("Select Rectangle")){textMode = false; makeOverlay = false; dragOp = -11; dirty = true; standardCursor();}
	    if(action.equals("Insert Overlay")){textMode = false; makeOverlay = true; dragOp = -11; dirty = true; standardCursor();}
	    //dragOp = 76  after the rectangle is selected, but before it is dragged
	    //dragOp = 77  dragging the selected rectangle
	    //dragOp = 78  distorting the selected rectangle
	    //dragOp = 79  after inserting the selected rectangle
	    if(action.equals("Eraser")) {textMode = false; dragOp = 3; dirty = true; boxCursor();}
	    //dragOp = 33  precision eraser
	    if(action.equals("Ruler")) {
		if(dragOp == -1) {
			dragOp = 0;
			gotool = true;
		}
		else dragOp = -1;
		textMode = false; dotCursor(); dirty = true;
	    }
	    if(action.equals("Top Eraser")) {textMode = false; dragOp = 4; dirty = true;}
	    if(action.equals("Top Razor")) {textMode = false; dragOp = 5; dirty = true; standardCursor();}
	    //if(action.equals("Bottom Eraser")) {textMode = false; dragOp = 5; dirty = true;}
	    //dragOp = 11  text menu operation from context menu
	    if(action.equals("Set Size")){
		activePage = 0;
		offX = 0;
		offY = 0;
		if(fullScreen){
			offX = foffX;
			offY = foffY;
		}
		if(thumbs && !threeup) jpages.setScale(2 * jpages.getScale());
		thumbs = false;
		threeup = false;
		poverlay = false;
		jpages.PO = 2.0;
		jpages.invalidateGraphics();
		//dragOp = 13; 
		dirty = true; 
		standardCursor();
		setup();
		dragOp = 13;
	    }
	    //set size is setting the size of the paper by drag and drop
	    //dragOp = 100 display is locked
	    //dragOp = 113 inserting of an image
	    //dragOp = 114 is inserting a paste
	    //dragOp = 117 is inserting a link
	    //dragOp = 200 is browse mode
	    if((dragOp != 0) && (dragOp != 100) && (dragOp != 200) && gotool) {
		actionMsg = action;
		//actionMsg will be displayed in the status panel
		jrnlPane.setStatus(action);
		return;
	    }

	    if(action.equals("Undo/Redo List")){
		new Jarnbox(gJrnlFrame, "Undo/Redo List", jarn, true).showUnRe();
	    }

	    if(action.equals("Manage Internal Files")){
		manageDialog();
	    }

	    if(action.equals("Trap Colors")){
		trapDialog();
	    }		

	    if(action.equals("Undo Recognition")){
		if(!analyze) return;
		jpages.undoRecog();
		dirty = true;
	    }

	    if(action.equals("Space")){
		textOp("type", 0, " ");
		return;
	    }
	    if(action.equals("Return")){
		textOp("type", 0, "\n");
		return;
	    }		
	    if(action.equals("Backspace")){
		textOp("extendEmpty", -1, null);
		textOp("type", 0, "");
		return;
	    }

	    if(action.equals("Recognize Page")){
		janalyzeinit();
		String str = jpages.analyzeAll(jt);
		textOp("type", 0, str);
		drawState = 0;
		//return;
	    }

	    if(action.equals("Sticky Ruler")){
		stickyRuler = !stickyRuler;
		srcb.setState(stickyRuler);
	    }

	    if(action.equals("Smooth Strokes")){
		smoothStrokes = !smoothStrokes;
		smstrk.setState(smoothStrokes);
	    }

//          These modes are buggy
	    if(action.equals("No Action rightButton")){
		rightButton = "No Action";
                dirty = true;
	    }
	    if(action.equals("Last Action rightButton")){
		if(lastAction != null){
			rightButton = lastAction;
                        dirty = true;
		}
	    }

	    if(action.equals("Button Pen rightButton")){
	        rightButton = "Button Pen";
                dirty = true;
	     }

            if(action.equals("Context Menu rightButton")){
	        rightButton = "Context Menu";
                dirty = true;
	    }

            if(action.equals("Eraser rightButton")){
	        rightButton = "Eraser";
                dirty = true;
	    }

            if(action.equals("Select Rectangle rightButton")){
	        rightButton = "Select Rectangle";
                dirty = true;
	    }

//            These modes are buggy
            if(action.equals("No Action middleButton")){
		middleButton = "No Action";
		dirty = true;
	    }

	    if(action.equals("Last Action middleButton")){

		if(lastAction != null){
			middleButton = lastAction;
			dirty = true;
		}
	    }

	    if(action.equals("Button Pen middleButton")){
	        middleButton = "Button Pen";
		dirty = true;
	    }

            if(action.equals("Context Menu middleButton")){
	        middleButton = "Context Menu";
                dirty = true;
	    }

            if(action.equals("Eraser middleButton")){
	        middleButton = "Eraser";
                dirty = true;
	    }

            if(action.equals("Select Rectangle middleButton")){
	        middleButton = "Select Rectangle";
                dirty = true;
	    }
	
	    if(action.equals("Recognize")){
		analyze = !analyze; 
		recog1.setState(analyze);
		recog2.setState(analyze);
		//Janalyze.train = trainrecog;
		janalyzeinit();
		return;
	    }

	    //this next doesn't exist anymore
	    if(action.equals("Train Recognition")){
		trainrecog = !trainrecog;
		train1.setState(trainrecog);
		train2.setState(trainrecog);
		//Janalyze.train = trainrecog;
		return;
	    }

	    if(action.equals("Edit Dictionaries")){
		//Janalyze.clearDictionary();
		janalyzeinit();
		JDictionaryEditor jd = new JDictionaryEditor();
		Jarnal min = jarn.microJarnal(jd.jpan, toolkit);
		Janalyze janal = min.jrnlPane.jpages.getanalyze(jt);
		janal.train = true;
		jd.janal = janal;
		janal.jdic = jd;
		jd.updateList1();
		return;
	    }

	    boolean penChanged = false;
	    if((jt.transparency == 255) && !jt.highlighter && !(jt.color.equals("white") && jt.type.equals("Fat"))){
		old_color = jt.color;
		old_width = jt.width;
	    }

	    if(action.equals("Choose Instrument Color")){
		JMenu pc = buildPenColorMenu();
		pc.getPopupMenu().show(jbo,0,28);
		return;
	    }

	    if(action.equals("Default Pen")) {
		jt.fullCopy(jtd);
		penChanged = true;
	    }
	    if(action.equals("Default Highlighter")) {
		//if(! ((jt.color.equals("white")) && (jt.type.equals("Fat")))) old_color = jt.color;
		jt.fullCopy(jth);
		penChanged = true;
	    }
	    if(action.equals("Set Default")) {
		boolean isHigh = false;
		if(jt.highlighter) isHigh = true;
		if(jt.transparency != 255) isHigh = true;
		if(isHigh) jth.fullCopy(jt);
		else jtd.fullCopy(jt);
		penChanged = true;
	    }
	    if(action.equals("Set Button Pen")){
		jtbu.fullCopy(jt);
		penChanged = true;
	    }
	    if(action.equals("Choose Pen")) {
		Jtool jtCopy = new Jtool();
		jtCopy.fullCopy(jt);
		Jtool.penDialogListener pdial = jtCopy.showDialog(gJrnlFrame, jt, jtd, jth, jtbu, jarn);
		penChanged = pdial.dirty;
		if(pdial.highlighterStyle != null) highlighterStyle = pdial.highlighterStyle;
		jpages.jtool.bWidth = jt.bWidth;
		jpages.jtool.fatWidth = jt.fatWidth;
		jpages.jtool.hTrans = jt.hTrans;
		jpages.jtool.setWidth(jpages.jtool.type);
	    }
	    if(action.equals("Set Fine")) {setWidth("Fine"); penChanged = true;}
	    if(action.equals("Set Medium")) {setWidth("Medium"); penChanged = true;}
	    if(action.equals("Set Heavy")) {setWidth("Heavy"); penChanged = true;}
	    if(action.equals("Set Fat")) {setWidth("Fat"); penChanged = true;}
	    if(action.equals("Bottom Highlighter")){
		highlighterStyle = "bottom";
		jt.highlighter = true;
		jt.setOpaque();
		penChanged = true;
	    }
	    if(action.equals("Translucent Highlighter")){
		highlighterStyle = "translucent";
		jt.highlighter = false;
		jt.setTranslucent();
		penChanged = true;
	    }
	    if(action.equals("Transparent Highlighter")){
		highlighterStyle = "transparent";
		jt.highlighter = false;
		jt.setTransparent();
		penChanged = true;
	    }
	    if(action.equals("Highlighter")){
		//if(! ((jt.color.equals("white")) && (jt.type.equals("Fat")))) old_color = jt.color;
		jt.highlighter = true;
		if(!highlighterStyle.equals("bottom")){
			jt.highlighter = false;
			jt.setTransparency(highlighterStyle);
		}
		penChanged = true;
	    }
	    if(action.equals("Pen")){
		jt.highlighter = false;
		jt.setOpaque();
		penChanged = true;
	    }
	    if(action.equals("Black") || action.equals("Blue") || action.equals("Red") || action.equals("Green") || action.equals("Magenta")){
		boolean isHigh = false;
		if(jt.highlighter) isHigh = true;
		if(jt.transparency != 255) isHigh = true;
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = action.toLowerCase();
		if(isHigh) {
			if(old_width == 1.0f) jt.width = jtd.width;
			else jt.width = old_width;
		}
		penChanged = true;
	    }
	    if(action.equals("Fine")){
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = old_color;
		setWidth("Fine");
		penChanged = true;
	    }
	    if(action.equals("Medium")){
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = old_color;
		setWidth("Medium");
		penChanged = true;
	    }
	    if(action.equals("Heavy")){
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = old_color;
		setWidth("Heavy");
		penChanged = true;
	    }
	    if(action.equals("Fat")){
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = old_color;
		setWidth("Fat");
		penChanged = true;
	    }
	    if(action.equals("Yellow Highlighter")){
		//if(! ((jt.color.equals("white")) && (jt.type.equals("Fat")))) old_color = jt.color;
		jt.highlighter = true;
		if(!highlighterStyle.equals("bottom")) {
			jt.highlighter = false;
			jt.setTransparency(highlighterStyle);
		}
		jt.color = "yellow";
		setWidth("Fat");
		penChanged = true;
	    }
	    if(action.equals("Magenta Highlighter")){
		//if(! ((jt.color.equals("white")) && (jt.type.equals("Fat")))) old_color = jt.color;
		jt.highlighter = true;
		if(!highlighterStyle.equals("bottom")) {
			jt.highlighter = false;
			jt.setTransparency(highlighterStyle);
		}
		jt.color = "magenta";
		setWidth("Fat");
		penChanged = true;
	    }
	    if(action.equals("White Out")){
		if(!jt.highlighter) old_color = jt.color;
		jt.highlighter = false;
		jt.setOpaque();
		jt.color = "white";
		setWidth("Fat");
		penChanged = true;
	    }
	    if(action.equals("black")) {jt.color = action; penChanged = true;}
	    if(action.equals("blue")) {jt.color = action; penChanged = true;}
	    if(action.equals("gray")) {jt.color = "dark gray"; penChanged = true;}
	    if(action.equals("green")) {jt.color = action; penChanged = true;}
	    if(action.equals("magenta")) {jt.color = action; penChanged = true;}
	    if(action.equals("orange")) {jt.color = action; penChanged = true;}
	    if(action.equals("pink")) {jt.color = action; penChanged = true;}
	    if(action.equals("red")) {jt.color = action; penChanged = true;}
	    if(action.equals("white")) {jt.color = action; penChanged = true;}
	    if(action.equals("yellow")) {jt.color = action; penChanged = true;}
	    if((jt.transparency == 255) && !jt.highlighter && !(jt.color.equals("white") && jt.type.equals("Fat"))) old_color = jt.color;
	    if(penChanged){
		textMode = false;
		setStatus("");
		setCursor();
		return;
	    }

	    if(action.equals("Highlight Lines")) jpages.highlightLines = !jpages.highlightLines;

            if(action.equals("Undo")) {
		//nextScrap = null;
		if(jpages.undo()) setup();
		dirty = true;
	    }
	    if(action.equals("Undo All")){
		jpages.setMark("tempundoall");
		jpages.untilMark("undo", "markthisdoesnotexist");
		setup();
		dirty = true;
	    }
            if(action.equals("Redo")) {
		if(jpages.redo()) setup();
		dirty = true;
	    }
	    if(action.equals("Redo All")){
		jpages.untilMark("redo", "marktempundoall");
		setup();
		dirty = true;
	    }	    

	    if(action.equals("Record")){
		jpages.recordingOn(!jpages.recordingOn());
		recbox.setState(jpages.recordingOn());
		return;
	    }

	    if(action.startsWith("Delete")){
		if(!pageList.isEmpty()){
			deletePages();
		}
		else if(textMode || !jpages.pageSelected()) action = "Clear";
		else {
			jpages.pageDelete();
			setup();
			dirty = true;
		}
	    }
		
	    if(action.startsWith("Clear")){
		if(textMode){
			textOp("extendEmpty", 1, null);
			textOp("type", 0, "");
			return;
		}
		if(jpages.pageSelected()){
			jpages.clearPage();
			setup();
			dirty = true;
		}
		else{
			clearRegion = true;
			dirty = true;
			drawState = 0;
			repaint();
			return;
		}
	    }
	
	    if(action.equals("Duplicate Page")){
		jpages.pageDup();
		setup();
		dirty = true;
	    }

	    if(action.equals("New Page Before")){
		jpages.setStartMark();
		jpages.pageBefore("");
		jpages.setSizeToBg();
		jpages.setEndMark();
		setup();
		dirty = true;
		jrnlPane.requestFocus();	    
	    }

	    if(action.equals("New Page")){
		jpages.setStartMark();
		jpages.pageAfter("");
		jpages.newBg();
		jpages.setSizeToBg();
		jpages.setEndMark();
		setup();
		dirty = true;
	    }

	    if(action.startsWith("Paste")){
		if(mini) return;
		String data = null;
		Image im = null;
		int dtype = -1;
		String jtype = null;
		Transferable contents;	
		if(!isApplet){
			Clipboard clip = toolkit.getSystemClipboard();
			contents=clip.getContents(gJrnlFrame);
		}
		else contents = internalClipboard;
		if(contents==null) System.out.println("empty clipboard");
		else{
			try{
				//DataFlavor dfs[] = contents.getTransferDataFlavors();
				//System.out.println("data flavors=" + dfs.length);
				//for(int ii = 0; ii < dfs.length; ii++) System.out.println(dfs[ii].getMimeType());
				dtype = -1; //nothing
				if(contents.isDataFlavorSupported(DataFlavor.stringFlavor)) dtype = 0; //plain text
				if(contents.isDataFlavorSupported(new DataFlavor("jaj/pair; class=java.lang.String", "Jarnal Clipboard Data")))dtype = 2; //jaj data
				if(contents.isDataFlavorSupported(DataFlavor.imageFlavor)) dtype = 6; //class java.awt.Image
				if(contents.isDataFlavorSupported(new DataFlavor("text/rtf; class=java.io.InputStream", "MS Word Text Data")))dtype = 0; //work-around for MS Word providing image data for text strings
				if(dtype >= 0) {
					if(dtype == 0) {
						data = (String) contents.getTransferData(DataFlavor.stringFlavor);
						jtype = "text";
					}
					if(dtype == 2) {
						String temp[] = (String[]) contents.getTransferData(new DataFlavor("jaj/pair; class=java.lang.String", "Jarnal Clipboard Data"));
						data = temp[0];
						jtype = temp[1];

					}
					if(dtype == 6){
						im = (Image) contents.getTransferData(DataFlavor.imageFlavor);
						data = "ok";
						jtype = "image";
					}
     					if(data==null) System.out.println("no usable data on clipboard");
				}
			}
			catch(IOException ex){System.err.println("IOException"); ex.printStackTrace(); data = null;} 
			catch(UnsupportedFlavorException ex){ System.err.println("UnsupportedFlavorException"); ex.printStackTrace(); data = null;}
		}
		if((data != null) && dtype >= 0){
			if(jtype.equals("text")) textOp("type", 0, data);
			if(jtype.equals("paper")) {
				Jpaper jpp = new Jpaper();
				jpp.setConf(data);
				if(!pageList.isEmpty()){
					jpages.setStartMark();
					Iterator ts = pageList.iterator();
					while(ts.hasNext()){
						int izi = -((Integer)ts.next()).intValue();
						int pzp = jpages.getPage() - 1;
						int deltaz = izi - pzp;
						if(pzp != 0) jpages.nextPage(deltaz);
						jpages.setPaper(jpp);
						invalidateP();
					}
					jpages.setEndMark();
				}
				else jpages.setPaper(jpp);
			}
			if(jtype.equals("page")) {
				if(jpages.pageSelected() && !textMode) jpages.replacePage(data);
				else {
					jpages.pageAfter(data);
					setup();
				}
			}
			if(jtype.equals("pages")) {
				jpages.setStartMark();
				String ter = UndoPage.terminator;
				int nf0 = 0;
				int nfn = 0;
				boolean firstIter = true;
				while(nf0 < data.length()){
					nfn = data.indexOf(ter, nf0);
					String tdata = data.substring(nf0, nfn);
					nf0 = nfn + ter.length();
					if(firstIter && jpages.pageSelected() && !textMode){
						jpages.replacePage(tdata);
						firstIter = false;
					}
					else {
						jpages.pageAfter(tdata);
					}
				}
				jpages.setEndMark();
				setup();
			}
			if(jtype.equals("object")){
				clipdata = data;
				dragOp = 114;
				standardCursor();
				setStatus("");
				return;
			}
			if(jtype.equals("image")){
				String oldScrap = nextScrap;
				try{
					nextScrap = jpages.addScrapImage(im);
				}
				catch(Exception ex){System.err.println(ex); nextScrap = oldScrap;}
				dragOp = 113;
				standardCursor();
				setStatus("");
				return;
			}
			dirty = true;
		}
		if(dtype == -1) System.out.println("Wrong flavor.");
		setup();
	    }

	    if(action.equals("Copy Paper")){
		String temp = jpages.getPaperCopyConf();
		JarnalSelection sel = new JarnalSelection(temp, "", temp, "paper");
		if(!isApplet){
			Clipboard clip = toolkit.getSystemClipboard();
			clip.setContents(sel, sel);
		}
		else internalClipboard = sel;
		action = "Done Copying Paper";
	    }

	    if(action.equals("Select All Text")){jpages.selectAllText();}

	    if(action.equals("Copy All Text")){
		StringSelection sel = new StringSelection(jpages.copyAllText());
		if(!isApplet){
			Clipboard clip = toolkit.getSystemClipboard();
			clip.setContents(sel, sel);
		}
		else internalClipboard = sel;
		action = "Done Copying All Text";
	    }

	    if(action.startsWith("Copy") || action.startsWith("Cut")){
		JarnalSelection sel = null;
		if(!pageList.isEmpty()){
			sel = jpages.copyPages(action, pageList);
			if(action.startsWith("Cut"))deletePages();
		}
		else{
			if(jpages.pageSelected() && !textMode){
				sel = jpages.copyPage(action, getFileName());
				if(action.startsWith("Cut")){
					jpages.pageDelete();
					setup();
					dirty = true;
				}
			}
			if(textMode){
				sel = jpages.clipText();
				if(action.startsWith("Cut")) textOp("type", 0, "");
			}
			if(!jpages.pageSelected() && !textMode){
				String temp = jpages.copyDragList();
				String dhtml = "";
				if(!isApplet){
					try{
						File gfile = File.createTempFile("jarnalClip", ".jpg");
						if(jpages.writeClippedGraphicFile(gfile, null, "jpg", false))
							dhtml = "<img src=\"file://" + gfile.getPath() + "\">";
					} catch(Exception ex){ex.printStackTrace();}
				}
				sel = new JarnalSelection(temp, dhtml, temp, "object");
				if(action.startsWith("Cut")){
					clearRegion = true;
					dirty = true;
					drawState = 0;
				}
			}
		}
		if(sel == null) return;			
		if(!isApplet){
			Clipboard clip = toolkit.getSystemClipboard();
			clip.setContents(sel, sel);
		}
		else internalClipboard = sel;
		if(textMode && pageList.isEmpty()) return;
	    }

	    if(makeInvalid) invalidateP();
	    if(pageChange == 0) choosepage(false, false);
            repaint();
            requestFocus();
	    setStatus("");
	}

	//used by the client to force screen redraws
	public void getdo(boolean test){
	    dirty = true;
	    drawState = 0;
            jrnlPane.repaint();
            jrnlPane.requestFocus();
	    setStatus("");
	    if(test) setup();		
	}

	public void setSave(boolean qck){
	    if(mini) return;
	    boolean jps[] = jpages.getStatus();
	    if(undoButton == null) return;
	    undoButton.setEnabled(jps[0]);
	    redoButton.setEnabled(jps[1]);
	    if(qck) return;
	    boolean sbse = dirty;
	    if(!isNetSave && fname.equals("")) sbse = dirty || true;
	    if(isNetSave && netServer.equals("")) sbse = false;
	    if(sbse) saveButton.setEnabled(true);
	    else saveButton.setEnabled(false);
	}

	public void setVQ(){
	   genQuality0.setState(false);
	   genQuality1.setState(false);
	   genQuality2.setState(false);
	   backQuality0.setState(false);
	   backQuality1.setState(false);
	   backQuality2.setState(false);
	   int bq = viewQuality % 16;
	   int fq = 16 * (viewQuality / 16);
	   if(bq == 0) backQuality0.setState(true);
	   if(bq == 1) backQuality1.setState(true);
	   if(bq == 2) backQuality2.setState(true);
	   if(fq == 0) genQuality0.setState(true);
	   if(fq == 0x40) genQuality1.setState(true);
	   if(fq == 0x80) genQuality2.setState(true);
        }

	public void setStatus(String stat){
	    if(mini) return;
	    String freemem = "";
	    if(!isApplet){
		Runtime rt = Runtime.getRuntime();
		float test = (float)rt.freeMemory() + (float)Jtool.maxMemory() - (float)rt.totalMemory();
		test = 100.0f - (100.0f *test/(float) Jtool.maxMemory());
		freemem = " &nbsp;&nbsp;" + Jarnal.trans("mem") + ": " + (int) test + "%";
		if(test > 90.0f) freemem = "<font color=red>" + freemem + "</font>";
	    }
	    String dstring = "";
	    String fstring = "";
	    setSave(false);
	    boolean jps[] = jpages.getStatus();
	    if(prevPageButton == null) return;
	    prevPageButton.setEnabled(jps[2]);
	    firstPageButton.setEnabled(jps[2]);
	    lastPageButton.setEnabled(jps[3]);
	    shbg.setState(jpages.showBackground());
	    rbh.setState(jpages.getRepeating());
	    if(textMode) fstring = " [Text]";
	    String tstring = jpages.getHtmlDesc();
	    tstring = Jtool.lastToHtml(tstring);
	    stat = "";
	    if(actionMsg != "") stat = "&lt;" + actionMsg + "&gt;";
	    if(dragOp == 113) stat = "&lt;" + Jarnal.trans("Click to insert image") + "&gt;";
	    if(dragOp == 114) stat = "&lt;" + Jarnal.trans("Click to paste") + "&gt;";
	    if(dragOp == 117) stat = "&lt;" + Jarnal.trans("Click to link") + "&gt;";
	    String pstatus = Jarnal.trans("Page");
 	    if(!textMode && jpages.pageSelected()) pstatus = "<font color=blue>" + Jarnal.trans("Page") + "</font>";
            if(!fullScreen) statusBar.setText("<html>" + pstatus + " " + jpages.getPage() + " " + Jarnal.trans("of") + " " + jpages.getPages() + " &nbsp;" + dstring + fstring + Jarnal.trans("Font") + ": " + tstring + " &nbsp;" + jt.htmlDesc() + " "  + stat + " &nbsp;" + jpages.getPaperDesc(absoluteScale) + serverMsg + " " + freemem + "</html>");
	    pageLabel.setText("" + jpages.getPage() + "/" + jpages.getPages());
	}

	public void setCursor(){
		if((jt.highlighter) || (jt.transparency != 255)) highCursor();
		else {
			if(jt.color.equals("white") && jt.type.equals("Fat")) whiteCursor();
			else dotCursor();
		}
		if(textMode)textCursor();
		if(dragOp == 200) handCursor();
	}

	private int twidth = -1;
	private int theight = -1;

	public void setTSize(){
		int test[] = jpages.getMaxSize(activePage, np());
		twidth = test[0];
		theight = test[1];
	}

	private int getTWidth(){
		if(twidth == -1) return jpages.getWidth();
		if(!thumbs) return jpages.getWidth();
		return twidth;
	}
	private int getTHeight(){
		if(theight == -1) return jpages.getHeight();
		if(!thumbs) return jpages.getHeight();
		return theight;
	}

	public void setOffset(){
		if(!thumbs){
			offX = 0;
			offY = 0;
			if(fullScreen){
				offX = foffX;
				offY = foffY;
			}
			return;
		}
		int w = getTWidth();
		int h = getTHeight();
		int r = activePage / nc();
		int c = activePage % nc();
		offX = w * c;
		offY = h * r;
	}

	public void zoom(float oldscale){
		float scale = jpages.getScale();
		if(oldscale == scale) return;
		Rectangle r = sp.getViewport().getViewRect();
		float z = scale/oldscale;
		Point p = new Point((int)(z * r.getX()), (int)(z * r.getY()));
		jpages.invalidateGraphics();
		setup(p);
	}

	//fits page to display
	public void resize() {
		float oldscale = jpages.getScale();
		Dimension d = null;
		if(barjarnal) d = new Dimension(tpanel.getDividerLocation(), tpanel.getHeight());
		else d = gJrnlFrame.getSize();
		if(embed) d = jarn.getSize();
		float dw = (float)(d.getWidth() - (2 * sbarSize));
		if(barjarnal) dw = dw + (float) sbarSize;
		else if((tpanel != null) && !showOutline) dw = dw - (float) tpanel.getDividerLocation();
		Rectangle gcb = null;
		if(fullScreen){
			gcb = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
			dw = (float) gcb.width;
		}			
		float scale = (float)(dw/jpages.getBaseWidth());
		if(!thumbs){
			dw = (float) (sp.getHeight() - 3);
			if(fullScreen) {
				dw = (float) (gcb.height - jtb1.getHeight() - (float) foffY);
				if(showOutline) dw = dw - (float) outheight - (float) foffX;
			}
			float tscale = (float)(dw/jpages.getBaseHeight());
			if(tscale < scale) scale = tscale;
		}
		scale = scale / nc();		
		if(scale != oldscale){
			jpages.setScale(scale);
			zoom(oldscale);
		}
		setup();
		//new fitwidth: always turn it off after using it
		fitWidth = false;
	}

	public void setup(){setup(null);}

	//if we change the number of pages, use setup
        public void setup(Point p){
		drawState = 0;
		//dragOp = 0;
		if(!((dragOp == -1) && stickyRuler))dragOp = 0;
		if(thumbs) activePage = jpages.getPage() - 1;
		//else activePage = 0;
		setTSize();
		setOffset();
		int w = getTWidth() * nc();
		int h = getTHeight() * nr();
		if(fullScreen){
			Rectangle gcb = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
			w = gcb.width;
			h = gcb.height - jtb1.getHeight();
			if(showOutline) h = h - outheight;
		}
		Dimension canvasSize = new Dimension(w, h);
		setSize(canvasSize);
		setPreferredSize(canvasSize);
		if(p == null) p = new Point(offX, offY);
		if((p != null) && (sp != null)) sp.getViewport().setViewPosition(p);
		p = null;
		if(locked) dragOp = 100;
        }



	private boolean pageIsVisible(int p){
		if(!thumbs) return true;
		int w = getTWidth();
		int h = getTHeight();
		int i = p / nc();
		int j = p % nc();
		Rectangle pr = new Rectangle(w*j, h*i, w, h);
		Rectangle vr = sp.getViewport().getViewRect();
		return vr.intersects(pr);

	}

	private void drawPage(Graphics2D g2, BufferedImage gg, int p){
		if(gg == null) return;
		if(!thumbs) return;
		int w = getTWidth();
		int h = getTHeight();
		if(g2 != null) {
			int i = p / nc();
			int j = p % nc();
			g2.setBackground(Color.white);
			g2.clearRect(w*j, h*i, w, h);
			g2.drawImage(gg, w*j, h*i, this);
			if(pageList.contains(new Integer(-p))){
				g2.setColor(Color.blue);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				g2.fillRect(w*j, h*i, w, h);
				g2.setComposite(AlphaComposite.SrcOver);
			}
		}

	}	

	private void setHints(Graphics2D gg2){
		gg2.setBackground(Color.white);
		bq = viewQuality % 16;
		int fq = viewQuality / 16;
		if((fq == 4) || (fq == 8)){
            		gg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            		gg2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            		gg2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);	
            		gg2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		}
		if(fq == 4){
			gg2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		}
	}

	private int firstPage(){
		return 0;
	}

	private int np(){
		return nc() * nr();
	}

	private int nr(){
		if(!thumbs) return 1;
		if(thumbs && threeup) return jpages.getPages();
		return (jpages.getPages() + 1) / 2;
	}

	private int nc(){
		if(thumbs && !threeup) return 2;
		return 1;
	}

	private LinkedList compactr(LinkedList ll, LinkedList rr){
		if(ll == null) ll = new LinkedList();
		for(int ii = 0; ii < rr.size(); ii++) ll = compactr(ll, (Rectangle)rr.get(ii));
		return ll;
	}

	private LinkedList compactr(LinkedList ll, Rectangle r){
		float fact = 1.2f;
		LinkedList ans = new LinkedList();
		for(int ii = 0; ii < ll.size(); ii++){
			Rectangle newr = (Rectangle)ll.get(ii);
			Rectangle allr = newr.union(r);
			int sizeold = (r.width * r.height) + (newr.width * newr.height);
			int sizenew = (int)((float)allr.width * (float)allr.height / fact);
			if(sizenew < sizeold) r = allr;
			else ans.add(newr);
		}
		ans.add(r);
		return ans;
	}
		

	private void updateCurrentPage(){
		jpages.setScale(jpages.getScale());
		int w = jpages.getWidth();
		int h = jpages.getHeight();
		gg = (BufferedImage) jrnlCreateImage(w, h);
		if(gg2 != null) gg2.dispose();
		gg2 = gg.createGraphics();
		setHints(gg2);
		gg2.clearRect(0,0,w,h);
		jpages.setGraphics2D(gg2);
		jpages.updatePage(gg2, gg, firstPage(), jpages.getPage() - 1, bq);
	}

	private void updatePage(Graphics2D g2, int i) {
		BufferedImage gbi = jpages.getGraphics(firstPage() + i);
		Graphics2D gbi2;
		boolean fooflag = false;
		if(gbi == null){
			float s = jpages.setScale(i);
			//gbi = (BufferedImage)jrnlCreateImage((int)(s * jpages.getWidth(i)), (int)(s * jpages.getHeight(i)));
			gbi = (BufferedImage)jrnlCreateImage(jpages.getWidthInt(i), jpages.getHeightInt(i));
			gbi2 = gbi.createGraphics();
			setHints(gbi2);
			jpages.updatePage(gbi2, gbi, firstPage(), i, bq);
			fooflag = true;
		}
		else {
			gbi2 = gbi.createGraphics();
			setHints(gbi2);
		}
		drawPage(g2, gbi, i);
		if(i == activePage) {
			gg = gbi;
			//if((gg2 != null) && fooflag) gg2.dispose();
			if(gg2 != null) gg2.dispose();
			gg2 = gbi2;
			jpages.setGraphics2D(gg2);
		}
		else if(gbi2 != null) gbi2.dispose();
		//it would be nice to draw some additional pages in the background at this point
		//but experimentation shows that drawing pages in the background interferes too much
		//with drawing on the screen
	}

	private BufferedImage jrnlCreateImage(int w, int h){
		BufferedImage hmg = null;
		hmg = jpages.getImage(w, h);
		if(hmg != null) return hmg;
		boolean flag = false;
		checkMemory();
		try{
			hmg = (BufferedImage)createImage(w, h);
		}
		catch(Error er){
			jpages.invalidateGraphics();
			flag = true;
		}
		if(flag){
			try{
				hmg = (BufferedImage)createImage(w, h);
			}
			catch(Error er){
				memoryerrorstring = "\n" + mymemory();
				jpages.setScale(jpages.getScale()/2);
				hmg = (BufferedImage)createImage(1,1);
				jtm.setMemoryError();
			}
		}
		return hmg;
	}

	boolean jpsetup = false;
	public Rectangle drawRect;

	public Point2D.Double lowercorner = null;
	public Point2D.Double uppercorner = null;
	public boolean hitLowerCorner(int x, int y){
		if(lowercorner == null) return false;
		int xx = (int) lowercorner.getX();
		int yy = (int) lowercorner.getY();
		int r = 8;
		boolean test = true;
		if(x < xx - r) test = false;
		if(x > xx + r) test = false;
		if(y < yy - r) test = false;
		if(y > yy + r) test = false;
		return test;
	}

	public boolean hitRectangle(int x, int y){
		if(uppercorner == null) return false;
		if(lowercorner == null) return false;
		int xx = (int) uppercorner.getX();
		if(x < xx) return false;
		xx = (int) lowercorner.getX();
		if(x > xx) return false;
		int yy = (int) uppercorner.getY();
		if(y < yy) return false;
		yy = (int) lowercorner.getY();
		if(y > yy) return false;
		return true;
	}

	public void resetRectangle(){
		dragOp = 76;
		xx = jpages.getDragRectX(xx, offX);
		yy = jpages.getDragRectY(yy, offY);
		if(invalidateFlag) return;
		setRectangle(null);
	}

	public void setRectangle(Graphics2D g2){
		int xmin = xx[0];
		int xmax = xx[1];
		int ymin = yy[0];
		int ymax = yy[1];
		clipR = new Rectangle(xmin-rad-offX, ymin-rad-offY, xmax - xmin + (2*rad), ymax - ymin + (2*rad));	
		drawState = -12;
		if(g2 == null) repaint(1l, xmin - rad, ymin - rad, xmax - xmin + (2*rad), ymax - ymin + (2*rad));
		else paintRectangle(g2);
	}

	public void paintRectangle(Graphics2D g2){
            	BufferedImage ggg;
	    	Graphics2D ggg2;
		if(gg == null) return;
		int w = (int) clipR.width;
		int h = (int) clipR.height;
		//the next can happen if the user flips the rectangle; in this case we drop him out of rectangle mode
		//since we don't really know how to draw the rectangle.
		if((w <= 0) || (h <= 0)) {
			dragOp = 0;
			drawState = 1;
			return;
		}
		int xxx = (int) clipR.getX();
		int yyy = (int) clipR.getY();
		ggg = (BufferedImage)createImage(w, h);
		ggg2 = ggg.createGraphics();
		ggg2.drawImage(gg, -xxx, -yyy, this);
		ggg2.setPaint(jt.getColor());
		BasicStroke bs = new BasicStroke(getStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		ggg2.setStroke(bs);
		boolean makeRect = false;
		if((drawState == -11) || (drawState == -12)) makeRect = true;
		int nz = 2;
		if(makeRect) nz = 5;
		int xxz[] = new int[nz];
		int yyz[] = new int[nz];
		xxz[0]=xx[0]; xxz[1]=xx[1]; yyz[0]=yy[0]; yyz[1]=yy[1];
		if(drawState == -1){
			int vdpi = Jpaper.dpi;
			if(absoluteScale) vdpi = Jpaper.adpi;
			double llength = Math.sqrt(((xx[0]-xx[1])*(xx[0]-xx[1])) + ((yy[0]-yy[1])*(yy[0]-yy[1])))/((double) jpages.getScale() * vdpi);
			java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
			actionMsg = "Ruler " + df.format(llength) + "\"";
			setStatus("");
		}
		int oldOffx = offX;
		int oldOffy = offY;
		offX = offX + xxx;
		offY = offY + yyy;
		boolean oldthumbs = thumbs;
		thumbs = true;
		if(makeRect) {
			int flop;
			if(xxz[0] > xxz [1]){
				flop = xxz[1];
				xxz[1] = xxz[0];
				xxz[0] = flop;
			}
			if(yyz[0] > yyz[1]){
				flop = yyz[1];
				yyz[1] = yyz[0];
				yyz[0] = flop;
			}
			lowercorner = new Point2D.Double(xxz[1], yyz[1]);
			uppercorner = new Point2D.Double(xxz[0], yyz[0]);
			//if(xxz[0] > xxz[1]) lowercorner = new Point2D.Double(xxz[0], yyz[1]);
		}
		xxz = offXL(xxz, 2, -1);
		yyz = offYL(yyz, 2, -1);
		thumbs = oldthumbs;
		if(makeRect){	
			xxz[4] = xxz[0]; yyz[4] = yyz[0]; xxz[3] = xxz[0]; yyz[3] = yyz[1];
			xxz[2] = xxz[1]; yyz[2] = yyz[1]; yyz[1] = yyz[0];
			ggg2.setPaint(Color.red);
			bs = new BasicStroke(0.5f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			ggg2.setStroke(bs);
			if(!makeOverlay){
				int dotwidth = 8;
				int x0x = xxz[2];
				int y0y = yyz[2];
				if(xxz[4] > xxz[2]) {
					x0x = xxz[3];
					y0y = yyz[3];
				}
				ggg2.fill(new Ellipse2D.Double(x0x - dotwidth, y0y - dotwidth, 2 * dotwidth, 2 * dotwidth));
			}
		}	
		ggg2.drawPolyline(xxz, yyz, nz);
		g2.drawImage(ggg, offX, offY, this);
		offX = oldOffx;
		offY = oldOffy;
		if(drawState == -1) drawState = 0;
		if(drawState == -12) drawState = 1;
		if(ggg2 != null) ggg2.dispose();
		return;
        }

	public void checkMemory(){
		if(Jarnal.isApplet) return;
		Runtime rt = Runtime.getRuntime();
		float test = (float)rt.freeMemory() + (float)Jtool.maxMemory() - (float)rt.totalMemory();
		test = 100.0f - (100.0f *test/(float) Jtool.maxMemory());
		if(test > 90.0f) {
			System.out.println("memory low; clearing caches");
			jpages.invalidateGraphics();
		}
	}

    	public void paintComponent(Graphics g){
	    if(g == null) {
		menuflag = false;
		return;
	    }
	    setBackground(Color.white);
	    super.paintComponent(g);	

            Graphics2D g2 = (Graphics2D) g;
	    if((dragOp > 0) && (dragOp != 11) && (dragOp != 76)) drawState = 0;
	    if(invalidateFlag){
		invalidateFlag = false;
		drawState = 0;
	    }

	    //quick draw of strokes as they come in
	    if(drawState == 2){
		menuflag = false;
		if(gg2 == null) {
			if(g2 != null) g2.dispose();
			return;
		}
		gg2.setPaint(jt.getPaint());
		BasicStroke bs = new BasicStroke(getStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		gg2.setStroke(bs);
		Rectangle r = (new Polygon(xxx, yyy, maxcc)).getBounds();
		if(thumbs || fullScreen){
			xxx = offXL(xxx, maxcc, -1);
			yyy = offYL(yyy, maxcc, -1);
		}
		gg2.drawPolyline(xxx, yyy, maxcc);
		int sw = (int) (getStroke() + 1);
		r = new Rectangle((int)(r.getX() - sw), (int)(r.getY() - sw), r.width + (2 * sw), r.height + (2 * sw));
		g2.setClip(r);
		g2.drawImage(gg, offX, offY, this);
		g2.setClip(null);
		drawState = 0;
		if(g2 != null) g2.dispose();
		return;
	    }
	    //drawing text in a clipping rectangle
	    if(drawState == 7){
		if((gg2 == null) || (clipR == null)) {
			if(g2 != null) g2.dispose();
			return;
		}
		int X = (int)(clipR.getX());
		int Y = (int)(clipR.getY());
		jpages.draw(gg2, X, Y, clipR.width, clipR.height);
		drawState = 0;
		if(!menuflag){
			menuflag = false;
			g2.setClip(X + offX, Y + offY, clipR.width, clipR.height);
			g2.drawImage(gg, offX, offY, this);
			g2.setClip(null);
			if(g2 != null) g2.dispose();
			return;
		}
	    }

	    menuflag = false;

	    // -1 is ruler, -11 is rectangular lasso
	    if((drawState == -1) || (drawState == -11) || (drawState == -12)) {
		paintRectangle(g2);
		if(g2 != null) g2.dispose();
		return;
	    }

	    if((thumbs) && (drawState == 0)) drawState = 100;
	    LinkedList bbs = null;
            if((drawState == 100) || (drawState == 0) || (drawState == 101)){
		if((thumbs || fullScreen) && (dragOp != 11)) x = offPL(x, cnt, -1);
		if(dragOp == 0) for(int i = 0; i < cnt; i++) jrnlPane.stroke(x[i]);
		if((dragOp == -1) && (cnt > 0)) jrnlPane.stroke(x[cnt - 1]);
		else if((dragOp != 0) && (cnt > 0) && (dragOp != 11)) bbs = jpages.dragOp(x[cnt - 1]);
		if(clearRegion){
			bbs = jpages.eraseDragList();
			clearRegion = false;
		}
		if(gg == null) bbs = null;
		if((drawState == 101) && (drawRect != null)){
			bbs = new LinkedList();
			bbs.add(drawRect);
		}	
		cnt = 0;	
	    	int w = jpages.getWidth();
            	int h = jpages.getHeight();
		// We create buffered images, if they don't exist,
		// or if we changed the page size.
		if (gg == null || gg.getWidth() != w || gg.getHeight() != h) {
			jpages.invalidate();
			updateCurrentPage();
			if(!jpsetup) Jpar.setGraphics(gg2);
		}
		setHints(gg2);
		setTSize();
		if((drawState == 100) && (bbs == null)){
			for(int i = 0; i < np(); i++){
				if(firstPage() + i < jpages.getPages()){
					if(pageIsVisible(i)) updatePage(g2, i);
				}
			}
			setStatus("");
			drawState = 1;
			if(dragOp == 76) setRectangle(g2);
			if(g2 != null) g2.dispose();
			return;
		}
		if (bbs == null) {
			gg = jpages.getGraphics();
			if(gg == null) {
				updateCurrentPage();
			}
			else {
				if(gg2 != null) gg2.dispose();
				gg2 = gg.createGraphics();
				setHints(gg2);
				jpages.setGraphics2D(gg2);
			}
		}
		else {
			bbs = compactr(null, bbs);
			for (Iterator i = bbs.iterator(); i.hasNext(); ) {
				Rectangle r = (Rectangle) i.next();
				gg2.clearRect(r.x,r.y,r.width,r.height);
				jpages.draw(gg2,r.x,r.y,r.width,r.height);
			}
		}
		this.requestFocus();
	    }
	    if((dragOp == 13) || ((drawState == 0) && (bbs == null))) setStatus("");
	    if(gg != null) {
		if(!thumbs) g2.drawImage(gg, offX, offY, this);
		else{
			for(int i = 0; i < np(); i++){
				if(firstPage() + i < jpages.getPages())
					if(pageIsVisible(i)) updatePage(g2, i);
			}
		}
	    }
	    drawState = 1;
	    if(dragOp == 76) setRectangle(g2);
	    if(g2 != null) g2.dispose();
        }

	public boolean isPDF = false;

	public synchronized void print(PrintJob pdf, jrnlPDFWriter pdfWriter) throws PrinterException {
		int w = 612;
		int h = 792;
		if(pdf != null){
			w = pdf.getPageDimension().width;
			h = pdf.getPageDimension().height;
		}
		PageFormat pf = new PageFormat();
		Paper pa = new Paper();
		double X = pa.getImageableX();
		double Y = pa.getImageableY();
		if(!alignToMargins){
			X = 0;
			Y = 0;
		}
		pa.setSize(w, h);
		pf.setPaper(pa);
		BufferedImage g = null;
		if(pdfWriter == null) g = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);	
		Graphics2D g2 = null;
		Runtime rt = Runtime.getRuntime();
		Graphics gg = null;
		isPDF = true;
		if(pdfWriter != null) isPDF = false;
		for(int ii = 0; ii < 1000; ii++){
			if(cancelPrint) return;
			if(ii >= jpages.getPages()) return;
			float test = (float)rt.freeMemory() + (float)Jtool.maxMemory() - (float)rt.totalMemory();
			test = 100.0f - (100.0f *test/(float) Jtool.maxMemory());
			if(test > 80.0f) jpages.invalidateGraphics();
			if(pdfWriter == null) {
				gg = pdf.getGraphics();
				g2 = g.createGraphics();
			}
			else {
				if(!bestFit){
					double W = jpages.getWidth(ii);
					if(W >= 0) {
						double H = jpages.getHeight(ii);
						float scale = 1.0f;	
						if(!absoluteScale) scale = 72.0f /((float) Jpaper.dpi);
						W = W * scale;
						H = H * scale;
						w = (int)((2 * X) + W);
						h = (int)((2 * Y) + H);
						pa.setSize(w, h);
						pf.setPaper(pa);
					}
				}
				g2 = pdfWriter.writePDF(null, "newPage", w, h);
				g2.setClip(null);
			}
			if(pdfWriter == null) {
				//if pdfwriter is being used then this will block out the background
				//if pdfwriter is not being used the background will be written on top of this
				g2.setBackground(Color.WHITE);
				g2.clearRect(0, 0, w, h);
			}
			int p = print(g2, pf, ii, pdfWriter);
			if(p == Printable.NO_SUCH_PAGE) return;
			if(pdfWriter == null) gg.drawImage(g, 0, 0, null);
			else pdfWriter.writePDF(g, "writePage", 0, 0);
		}
		if(g2 != null) g2.dispose();
		isPDF = false;
	}

	public boolean cancelPrint = false;

	public void jbcancelmsg(String msg){
		if(jbcancel != null) jbcancel.msg.setText(msg);
		else System.out.println(msg);
	}

	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException{
		return print(g, pf, pi, null);
	}

    	public synchronized int print(Graphics g, PageFormat pf, int pi, jrnlPDFWriter pdfWriter) throws PrinterException {
		if(cancelPrint) return Printable.NO_SUCH_PAGE;
		double W = jpages.getWidth(pi);
		if(W < 0) return Printable.NO_SUCH_PAGE;
		//jbcancel.msg.setText("Printing page " + (pi + 1));
		jbcancelmsg("Printing page " + (pi + 1));		
		double H = jpages.getHeight(pi);

            	Graphics2D g2 = (Graphics2D) g;
	    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);	
            	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		double width = pf.getWidth();
		double height = pf.getHeight();
		double X = 0;
		double Y = 0;


		if(alignToMargins){
			width = pf.getImageableWidth();
			height = pf.getImageableHeight();
			X = pf.getImageableX();
			Y = pf.getImageableY();
			AffineTransform at = new AffineTransform(1.0, 0.0, 0.0, 1.0, X, Y);
			g2.transform(at);

		}
		float scale = 72.0f /((float) Jpaper.dpi);
		if(bestFit){	
			double ws = width/W;
			double hs = height/H;
			scale = (float) Math.min(ws,hs);
		}
		else if(absoluteScale) scale = 1.0f;

		if(isPDF && !alignToMargins && bestFit){
			Y = -50;
			X = 0;
			scale = 1.06f * scale;
			AffineTransform at = new AffineTransform(1.0, 0.0, 0.0, 1.0, X, Y);
			g2.transform(at);
		}


		if(pdfWriter != null) pdfWriter.setMargins(X, Y);

		if(jpages.print(g2, pi, scale, withBorders, pdfWriter)) {
			String pn = "" + (pi + 1);
			g2.setPaint(Color.darkGray);
			if(showPageNumbers) g2.drawString(pn, 20, 30);
			if(g2 != null) g2.dispose();
			return Printable.PAGE_EXISTS;
		}
		else {
			if(g2 != null) g2.dispose();
			return Printable.NO_SUCH_PAGE;
		}
	}
	public void writeGraphicFile(File f, String type){
	    jpages.writeGraphicFile(f, null, type, withBorders);
	}
	public void writeTIFFGraphicFile(File f){
	    jpages.writeTIFFGraphicFile(f, null, withBorders);
	} 
    }
}

