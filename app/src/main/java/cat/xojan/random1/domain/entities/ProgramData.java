package cat.xojan.random1.domain.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgramData {

    @SerializedName("result")
    private List<Program> programs;

    private String success;

    public List<Program> getPrograms() {
        return programs;
    }

    public String getSuccess() {
        return success;
    }
}
