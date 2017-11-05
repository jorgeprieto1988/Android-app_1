package com.appadia.beacon.SQL;

import java.util.HashMap;
import java.util.Map;

public class CheckFbAuth {
	private String fbid;
    private String name;
    private String link;
    private String piclink;
    private String email;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public CheckFbAuth(String fbid, String name, String link, String piclink, String email) {
		setFbId(fbid);
        setName(name);
        setLink(link);
        setPicLink(piclink);
        setEmail(email);
	}

	public String getFbId() {
		return fbid;
	}

	public void setFbId(String _fbid) {
		this.fbid = _fbid;
	}

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }
    
    public String getLink() {
        return link;
    }

    public void setLink(String _link) {
        this.link = _link;
    }
    
    public String getPicLink() {
        return piclink;
    }

    public void setPicLink(String _piclink) {
        this.piclink = _piclink;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
