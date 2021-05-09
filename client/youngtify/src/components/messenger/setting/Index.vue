<template>
  <div class="md:pl-16 pt-16">
    <div class="w-full md:w-full lg:w-3/4 p-6 mx-auto">
      <div class="intro-y text-xl font-medium">Settings</div>
      <div class="grid grid-cols-12 gap-6 mt-5">
        <div class="intro-y col-span-12 lg:col-span-6">
          <div class="intro-y box p-4">
            <div>
              <label for="setting-form-1" class="form-label">Photo</label>
              <div
                class="image-upload border shadow-sm relative flex flex-col items-center justify-center rounded-md py-8 mt-3"
              >
                <div
                  class="image-upload__icon w-12 h-12 rounded-full flex items-center justify-center"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="1"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    class="feather feather-image w-5 h-5"
                  >
                    <rect
                      x="3"
                      y="3"
                      width="18"
                      height="18"
                      rx="2"
                      ry="2"
                    ></rect>
                    <circle cx="8.5" cy="8.5" r="1.5"></circle>
                    <polyline points="21 15 16 10 5 21"></polyline>
                  </svg>
                </div>
                <div class="image-upload__info mt-2">
                  Choose your group profile photo
                </div>
                <input
                  type="file"
                  class="w-full h-full top-0 left-0 absolute opacity-0"
                  id="setting-form-1"
                />
              </div>
            </div>
            <div class="mt-3">
              <label for="setting-form-2" class="form-label">Name</label>
              <input type="text" class="form-control" id="setting-form-2" />
            </div>
            <div class="mt-3">
              <label for="setting-form-3" class="form-label">Phone</label>
              <input type="text" class="form-control" id="setting-form-3" />
            </div>
            <div class="mt-3">
              <label for="setting-form-4" class="form-label">Email</label>
              <input type="text" class="form-control" id="setting-form-4" />
            </div>
            <div class="mt-3">
              <label for="setting-form-5" class="form-label">Bio</label>
              <textarea
                class="form-control"
                rows="5"
                id="setting-form-5"
              ></textarea>
            </div>
            <button class="btn btn-primary w-full mt-3">Save Settings</button>
          </div>
        </div>
        <div class="intro-y col-span-12 lg:col-span-6">
          <div class="intro-y box p-4">
            <v-text-field
              label="Outlined"
              placeholder="Placeholder"
              outlined
              dense
            ></v-text-field>
            <div>
              <v-text-field
                label="Outlined"
                placeholder="Placeholder"
                outlined
                dense
              ></v-text-field>
            </div>
            <div>
              <v-text-field
                label="Outlined"
                placeholder="Placeholder"
                outlined
                dense
              ></v-text-field>
            </div>
            <button class="btn btn-primary w-full mt-4">Change Password</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Setting",
};
</script>

<style>
</style>