#pragma version(1)
#pragma rs java_package_name(idv.kuma.app.samplerenderscript)
#pragma rs_fp_relaxed

#include "rs_debug.rsh"

int count;
int *keyCount;

struct Plus {
        int a;
        int b;
};

void root(const struct Plus *in, int* out) {
    count++;
    //rsDebug("RS a=", (int) keyCount[0]);
    //rsDebug("RS a=", (int) count);
    //Sample 4
    keyCount[0]++;
}