package com.delectable.mobile.util;

//This is mapped directly to attrs.xml's fontName enum, changes here should be reflected there as well
public enum Font {

    WHITNEY_BLACK("Whitney-Black-Adv.otf"),
    WHITNEY_BLACK_ITAL("Whitney-BlackItal-Adv.otf"),
    WHITNEY_BOLD("Whitney-Bold-Adv.otf"),
    WHITNEY_BOLD_ITAL("Whitney-BoldItal-Adv.otf"),
    WHITNEY_BOLD_SC("Whitney-BoldSC.otf"),
    WHITNEY_BOOK("Whitney-Book-Adv.otf"),
    WHITNEY_BOOK_ITAL("Whitney-BookItal-Adv.otf"),
    WHITNEY_BOOK_SC("Whitney-BookSC.otf"),
    WHITNEY_LIGHT("Whitney-Light-Adv.otf"),
    WHITNEY_LIGHT_ITAL("Whitney-LightItal-Adv.otf"),
    WHITNEY_MEDIUM("Whitney-Medium-Adv.otf"),
    WHITNEY_MEDIUM_ITAL("Whitney-MediumItal-Adv.otf"),
    WHITNEY_MEDIUM_SC("Whitney-MediumSC.otf"),
    WHITNEY_SEMIBOLD("Whitney-Semibold-Adv.otf"),
    WHITNEY_SEMIBOLD_ITAL("Whitney-SemiboldItal-Adv.otf"),
    WHITNEY_SEMIBOLD_SC("Whitney-SemiboldSC.otf"), ;

    private String mFileName;

    private Font(String fileName) {
        mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }

}
