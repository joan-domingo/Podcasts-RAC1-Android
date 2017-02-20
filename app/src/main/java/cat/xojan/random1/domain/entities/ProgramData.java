package cat.xojan.random1.domain.entities;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgramData {

    @SerializedName("result")
    private List<Program> programs;

    public List<Program> getPrograms() {
        return programs;
    }

    @VisibleForTesting
    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
