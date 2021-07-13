package ro.internals.rssviewer;

public enum RSSTaskStatus {
    NOT_DOWNLOADED, DOWNLOADED, DOWNLOADING, ERROR, UNDEFINED;

    int getCode() {
        switch (this) {
            case NOT_DOWNLOADED:
                return 0;

            case DOWNLOADED:
                return 1;

            case DOWNLOADING:
                return 2;

            case ERROR:
                return 3;

            case UNDEFINED:
                return 4;

            default:
                return -1;
        }
    }

    static RSSTaskStatus parse(int statusCode) {
        switch (statusCode) {
            case 0:
                return NOT_DOWNLOADED;

            case 1:
                return DOWNLOADED;

            case 2:
                return DOWNLOADING;

            case 3:
                return ERROR;

            case 4:

            default:
                return UNDEFINED;
        }
    }
}
