package de.chojo.sqlutil.updater;

class Patch {
    private final int major;
    private final int patch;
    private final String query;

    public Patch(int major, int patch, String query) {
        this.major = major;
        this.patch = patch;
        this.query = query;
    }

    public int major() {
        return major;
    }

    public int patch() {
        return patch;
    }

    public String query() {
        return query;
    }
}
