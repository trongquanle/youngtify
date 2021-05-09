<template>
    <div class="w-4/5 pb-4 h-32 justify-self-center cursor-wait">
      <div
        class="w-full h-full flex items-center justify-between bc-cpn-global br-global"
      >
        <div class="h-full w-3/5 flex items-center">
          <!-- avatar loadding -->
          <div
            class="animate-pulse m-4 rounded-full w-20 h-20 bg-gray-500"
          ></div>
          <!-- info loadding -->
          <div class="w-3/5">
            <div
              class="animate-pulse bg-gray-500 h-5 w-4/5 my-2 rounded-md"
            ></div>
            <div
              class="animate-pulse bg-gray-500 h-4 w-3/5 my-2 rounded-md"
            ></div>
            <div
              class="animate-pulse bg-gray-500 h-4 w-full my-2 rounded-md"
            ></div>
          </div>
        </div>
        <div class="h-full flex items-center justify-center gap-4">
          <div
            class="animate-pulse bg-gray-500 h-4 w-20 h-8 my-2 rounded-md"
          ></div>
          <div
            class="animate-pulse bg-gray-500 h-4 w-20 h-8 my-2 rounded-md"
          ></div>
        </div>
      </div>
    </div>
</template>

<script>
export default {
    name: "FriendSkeleton"
}
</script>

<style>

</style>