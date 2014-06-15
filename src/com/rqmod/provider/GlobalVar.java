package com.rqmod.provider;

public class GlobalVar {
	 
    private GlobalVar() {
		super();
		this.userid = BLANK;
		CellphoneNumber = CELLPHONENUM;
		this.app = null;
	}

	private int userid; 
    private String CellphoneNumber;
    private String token;
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private static GlobalVar app;
      
    public String getCellphoneNumber() {
		return CellphoneNumber;
	}

	public void setCellphoneNumber(String cellphoneNumber) {
		CellphoneNumber = cellphoneNumber;
	}

    public int getUserId() {  
            return userid;  
    }  

    public void setUserId(int userid) {  
            this.userid = userid;  
    }  
      
    private static final int BLANK = -1;  
    private static final String CELLPHONENUM = "";
    
    public static GlobalVar getInstance()
    {
    	if(null == app)
    	{
    		app = new GlobalVar();
    	}
    	return app;
    }
}
