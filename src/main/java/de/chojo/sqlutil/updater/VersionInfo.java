package de.chojo.sqlutil.updater;

class VersionInfo {
    private final int version;
    private final int patch;

    public VersionInfo(int version, int patch) {
        this.version = version;
        this.patch = patch;
    }

    public int version() {
        return version;
    }

    public int patch() {
        return patch;
    }
}
