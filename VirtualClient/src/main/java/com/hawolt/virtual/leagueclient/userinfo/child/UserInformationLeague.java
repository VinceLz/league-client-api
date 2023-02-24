package com.hawolt.virtual.leagueclient.userinfo.child;

import org.json.JSONObject;

/**
 * Created: 10/01/2023 22:15
 * Author: Twitter @hawolt
 **/

public class UserInformationLeague {

    private final long cuid, uid;
    private final Object apid;
    private final String cpid, pid, ploc;
    private final boolean lp, active;

    public UserInformationLeague(JSONObject object) {
        this.cuid = object.getLong("cuid");
        this.cpid = object.getString("cpid");
        this.uid = object.getLong("uid");
        this.pid = object.getString("pid");
        this.apid = object.get("apid");
        this.ploc = object.getString("ploc");
        this.lp = object.getBoolean("lp");
        this.active = object.getBoolean("active");
    }

    public long getCUID() {
        return cuid;
    }

    public long getUID() {
        return uid;
    }

    public String getCPID() {
        return cpid;
    }

    public String getPID() {
        return pid;
    }

    public Object getAPID() {
        return apid;
    }

    public String getPLOC() {
        return ploc;
    }

    public boolean isLp() {
        return lp;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "UserInformationLeague{" +
                "cuid=" + cuid +
                ", uid=" + uid +
                ", cpid='" + cpid + '\'' +
                ", pid='" + pid + '\'' +
                ", apid='" + apid + '\'' +
                ", ploc='" + ploc + '\'' +
                ", lp=" + lp +
                ", active=" + active +
                '}';
    }
}
