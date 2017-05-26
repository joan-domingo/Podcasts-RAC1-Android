package cat.xojan.random1.injection.module;

import cat.xojan.random1.ui.activity.RadioPlayerActivity;
import dagger.Module;

@Module
public class RadioPlayerModule {

    private final RadioPlayerActivity mActivity;

    public RadioPlayerModule(final RadioPlayerActivity radioPlayerActivity) {
        mActivity = radioPlayerActivity;
    }
}
