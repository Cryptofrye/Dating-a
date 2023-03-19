package com.thinkin_service.provider.ui.activity.help;


import com.thinkin_service.provider.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
